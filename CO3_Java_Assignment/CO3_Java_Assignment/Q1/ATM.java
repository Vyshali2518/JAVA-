import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class InsufficientFundsException extends Exception {
    InsufficientFundsException(String message) { super(message); }
}
class InvalidWithdrawalException extends IllegalArgumentException {
    InvalidWithdrawalException(String message) { super(message); }
}
public class ATM {
    private double balance = 5000.0;

    private void withdraw(String amountText, Path cardFile)
            throws IOException, InsufficientFundsException {
        try {
            if (!Files.exists(cardFile)) throw new IOException("Card-reader file is missing");
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) throw new InvalidWithdrawalException("Amount must be positive");
            if (amount > balance) throw new InsufficientFundsException("Insufficient balance");
            balance -= amount;
            System.out.printf("Withdrawal successful: %.2f; balance: %.2f%n", amount, balance);
        } catch (NumberFormatException | InvalidWithdrawalException e) {
            System.out.println("Input error: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Transaction declined: " + e.getMessage());
            throw e;
        } catch (IOException e) {
            System.out.println("Card error: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("ATM transaction attempt finished.");
        }
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        Path card = Path.of("card-reader.dat");
        try {
            Files.writeString(card, "CARD-READER-READY");
            atm.withdraw("1000", card);
            atm.withdraw("abc", card);
            atm.withdraw("-20", card);
            atm.withdraw("9000", card);
            Files.deleteIfExists(card);
            atm.withdraw("100", card);
        } catch (IOException | InsufficientFundsException e) {
            System.out.println("Handled by caller: " + e.getMessage());
        }
    }
}
