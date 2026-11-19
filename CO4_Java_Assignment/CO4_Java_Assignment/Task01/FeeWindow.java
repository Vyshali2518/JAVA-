import java.awt.*;
import java.awt.event.*;

public class FeeWindow extends Frame {
    private final TextField regNo = new TextField(18);
    private final TextField amount = new TextField(18);
    private final Label status = new Label("Enter fee details.");
    private final FeeService service = new FeeService();

    public FeeWindow() {
        super("Student Fee Payment");
        setLayout(new GridLayout(4, 2, 8, 8));
        add(new Label("Register number:")); add(regNo);
        add(new Label("Amount (INR):")); add(amount);
        Button pay = new Button("Pay");
        Button clear = new Button("Clear");
        Button receipt = new Button("Receipt");
        add(pay); add(clear); add(receipt); add(status);
        pay.addActionListener(e -> doPayment());
        clear.addActionListener(e -> { regNo.setText(""); amount.setText(""); status.setText("Cleared."); });
        receipt.addActionListener(e -> status.setText("Receipt: " + regNo.getText().trim()));
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setSize(440, 210);
    }

    private void doPayment() {
        try { status.setText(service.pay(regNo.getText(), amount.getText())); }
        catch (IllegalArgumentException ex) { status.setText(ex.getMessage()); }
    }

    public static void main(String[] args) { new FeeWindow().setVisible(true); }
}
