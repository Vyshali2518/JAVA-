import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LibrarySearchScreen extends JFrame {
    private final JTextField query = new JTextField(22);
    private final JComboBox<String> category =
        new JComboBox<>(new String[]{"All", "Book", "Journal", "Thesis"});
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public LibrarySearchScreen() {
        super("Campus Library Search");
        setLayout(new BorderLayout(8, 8));
        JLabel title = new JLabel("Campus Library Search", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(18f));

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Search:"));
        search.add(query);
        search.add(category);
        JButton searchButton = new JButton("Search");
        search.add(searchButton);
        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.NORTH);
        header.add(search, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        JList<String> results = new JList<>(model);
        results.setVisibleRowCount(10);
        add(new JScrollPane(results), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prev = new JButton("Prev");
        JButton next = new JButton("Next");
        JButton reserve = new JButton("Reserve");
        bottom.add(prev); bottom.add(next); bottom.add(reserve);
        JLabel status = new JLabel("Ready");
        JPanel footer = new JPanel(new BorderLayout());
        footer.add(bottom, BorderLayout.CENTER);
        footer.add(status, BorderLayout.SOUTH);
        add(footer, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> {
            model.clear();
            String term = query.getText().trim();
            if (term.isEmpty()) status.setText("Enter a search term.");
            else {
                model.addElement("Demo result for: " + term + " (" + category.getSelectedItem() + ")");
                status.setText("1 result displayed (sample data).");
            }
        });
        prev.addActionListener(e -> status.setText("Previous page selected."));
        next.addActionListener(e -> status.setText("Next page selected."));
        reserve.addActionListener(e -> status.setText("Select a result before reserving."));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(480, 320));
        setSize(680, 460);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchScreen().setVisible(true));
    }
}
