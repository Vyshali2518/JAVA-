import java.awt.FlowLayout;
import java.awt.event.*;
import javax.swing.*;

public class ConverterInnerClass extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");
    public ConverterInnerClass() {
        super("Converter – Inner Class");
        setLayout(new FlowLayout());
        JButton button = new JButton("Convert");
        add(new JLabel("INR")); add(amount); add(target); add(button); add(result);
        ConvertHandler handler = new ConvertHandler();
        button.addActionListener(handler); amount.addActionListener(handler);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); pack();
    }
    private class ConvertHandler implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            try {
                double n = Double.parseDouble(amount.getText().trim());
                if (!Double.isFinite(n) || n < 0) throw new NumberFormatException();
                double r = "USD".equals(target.getSelectedItem()) ? .012 : .011;
                result.setText(String.format("%.2f %s", n*r, target.getSelectedItem()));
            } catch (NumberFormatException ex) { result.setText("Invalid amount"); }
        }
    }
    public static void main(String[] a) { SwingUtilities.invokeLater(() -> new ConverterInnerClass().setVisible(true)); }
}
