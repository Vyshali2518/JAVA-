public class FeeService {
    public String pay(String regNo, String amountText) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Enter registration number.");
        }
        final double amount;
        try {
            amount = Double.parseDouble(amountText.trim());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Enter a valid amount.");
        }
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        // Demonstration service: replace this section with parameterized JDBC in deployment.
        return String.format("Payment recorded for %s: INR %.2f", regNo.trim(), amount);
    }
}
