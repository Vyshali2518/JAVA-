import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EventRegistrationApp extends JFrame {
    private final RegistrationService service = new RegistrationService();
    private final JTextField regNo = new JTextField(12), studentName = new JTextField(14);
    private final JComboBox<String> event = new JComboBox<>(new String[]{"Tech Fest", "Cultural Fest", "Sports Meet"});
    private final JComboBox<String> filter = new JComboBox<>(new String[]{"All", "Tech Fest", "Cultural Fest", "Sports Meet"});
    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "Register No", "Student Name", "Event", "Registered At"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready");
    private final JButton registerButton = new JButton("Register");
    private boolean workerRunning = false;
    private int page = 0;
    private static final int PAGE_SIZE = 100;

    public EventRegistrationApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout(8, 8));
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem refresh = new JMenuItem("Refresh");
        JMenuItem export = new JMenuItem("Export CSV");
        JMenuItem exit = new JMenuItem("Exit");
        file.add(refresh); file.add(export); file.addSeparator(); file.add(exit); bar.add(file); setJMenuBar(bar);
        refresh.addActionListener(e -> loadRows()); export.addActionListener(e -> exportCsv());
        exit.addActionListener(e -> requestClose());

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));
        form.setBorder(BorderFactory.createTitledBorder("New registration"));
        form.add(new JLabel("Register No:")); form.add(regNo);
        form.add(new JLabel("Student Name:")); form.add(studentName);
        form.add(new JLabel("Event:")); form.add(event); form.add(registerButton);
        add(form, BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new BorderLayout(5, 5));
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton filterButton = new JButton("Filter");
        JButton previous = new JButton("Previous");
        JButton next = new JButton("Next");
        JButton exportButton = new JButton("Export CSV");
        controls.add(new JLabel("Event:")); controls.add(filter); controls.add(filterButton);
        controls.add(previous); controls.add(next); controls.add(exportButton);
        listPanel.add(controls, BorderLayout.NORTH);
        listPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(listPanel, BorderLayout.CENTER); add(status, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> register());
        filterButton.addActionListener(e -> { page = 0; loadRows(); });
        previous.addActionListener(e -> { if (page > 0) { page--; loadRows(); } });
        next.addActionListener(e -> { page++; loadRows(); });
        exportButton.addActionListener(e -> exportCsv());
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { requestClose(); }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(900, 520); setLocationRelativeTo(null);
        loadRows();
    }

    private void register() {
        String r = regNo.getText(), n = studentName.getText(), ev = (String) event.getSelectedItem();
        if (r.trim().isEmpty() || n.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Register number and student name are required.", "Invalid input", JOptionPane.WARNING_MESSAGE);
            return;
        }
        setBusy(true);
        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception { service.register(r, n, ev); return null; }
            @Override protected void done() {
                setBusy(false);
                try { get(); status.setText("Registration saved."); JOptionPane.showMessageDialog(EventRegistrationApp.this, "Registration successful."); loadRows(); }
                catch (Exception ex) {
                    Throwable cause = ex.getCause();
                    String msg = cause == null ? ex.getMessage() : cause.getMessage();
                    if (hasSqlState23505(ex) || (msg != null && msg.toLowerCase().contains("duplicate")))
                        JOptionPane.showMessageDialog(EventRegistrationApp.this, "This student is already registered for this event.", "Duplicate", JOptionPane.WARNING_MESSAGE);
                    else JOptionPane.showMessageDialog(EventRegistrationApp.this, "Registration failed: " + msg, "Error", JOptionPane.ERROR_MESSAGE);
                    status.setText("Registration failed.");
                }
            }
        }.execute();
    }

    private boolean hasSqlState23505(Throwable t) {
        while (t != null) { if (t instanceof SQLException && "23505".equals(((SQLException)t).getSQLState())) return true; t = t.getCause(); }
        return false;
    }

    private void loadRows() {
        setBusy(true);
        String selected = (String) filter.getSelectedItem();
        int offset = page * PAGE_SIZE;
        new SwingWorker<List<Registration>, Void>() {
            @Override protected List<Registration> doInBackground() throws Exception {
                return service.findByEvent(selected, PAGE_SIZE, offset);
            }
            @Override protected void done() {
                setBusy(false);
                try {
                    List<Registration> rows = get(); model.setRowCount(0);
                    for (Registration x : rows) model.addRow(new Object[]{x.getId(), x.getRegisterNo(), x.getStudentName(), x.getEventName(), x.getRegisteredAt()});
                    status.setText("Loaded " + rows.size() + " row(s), page " + (page + 1) + ".");
                } catch (Exception ex) { status.setText("Load failed: " + ex.getMessage()); }
            }
        }.execute();
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("registrations.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        try (BufferedWriter out = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            for (int c = 0; c < model.getColumnCount(); c++) { if (c > 0) out.write(","); out.write(csv(model.getColumnName(c))); }
            out.newLine();
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 0; c < model.getColumnCount(); c++) { if (c > 0) out.write(","); out.write(csv(String.valueOf(model.getValueAt(r, c)))); }
                out.newLine();
            }
            status.setText("Exported current page to " + file.getName());
        } catch (IOException ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Export error", JOptionPane.ERROR_MESSAGE); }
    }
    private String csv(String value) { return "\"" + value.replace("\"", "\"\"") + "\""; }
    private void setBusy(boolean busy) { workerRunning = busy; registerButton.setEnabled(!busy); status.setText(busy ? "Working..." : status.getText()); }
    private void requestClose() {
        if (workerRunning) {
            int answer = JOptionPane.showConfirmDialog(this, "A database operation is in progress. Close anyway?", "Confirm close", JOptionPane.YES_NO_OPTION);
            if (answer != JOptionPane.YES_OPTION) return;
        }
        dispose();
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new EventRegistrationApp().setVisible(true)); }
}
