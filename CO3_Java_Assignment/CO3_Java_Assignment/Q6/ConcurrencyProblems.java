import java.util.concurrent.atomic.AtomicInteger;

class Wallet {
    final int id;
    private int balance;
    Wallet(int id, int balance) { this.id=id; this.balance=balance; }
    int getBalance() { return balance; }
    static void transfer(Wallet from, Wallet to, int amount) {
        Wallet first = from.id < to.id ? from : to;
        Wallet second = from.id < to.id ? to : from;
        synchronized (first) {
            synchronized (second) {
                if (amount <= 0 || from.balance < amount) throw new IllegalArgumentException("Invalid transfer");
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }
}
class TokenCounter {
    private final int[] buffer = new int[5];
    private int count = 0, in = 0, out = 0;
    public synchronized void put(int token) throws InterruptedException {
        while (count == buffer.length) wait();
        buffer[in] = token;
        in = (in + 1) % buffer.length;
        count++;
        System.out.println("Produced " + token + "; count=" + count);
        notifyAll();
    }
    public synchronized int take() throws InterruptedException {
        while (count == 0) wait();
        int token = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        System.out.println("Consumed " + token + "; count=" + count);
        notifyAll();
        return token;
    }
}
public class ConcurrencyProblems {
    public static void main(String[] args) throws InterruptedException {
        Wallet a = new Wallet(1, 1000), b = new Wallet(2, 1000);
        Thread t1 = new Thread(() -> Wallet.transfer(a, b, 100));
        Thread t2 = new Thread(() -> Wallet.transfer(b, a, 50));
        t1.start(); t2.start(); t1.join(); t2.join();
        System.out.println("Wallet balances: " + a.getBalance() + ", " + b.getBalance());

        TokenCounter counter = new TokenCounter();
        int[] collected = new int[2];
        Thread cook = new Thread(() -> {
            try { for (int t = 1; t <= 20; t++) counter.put(t); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread[] students = new Thread[2];
        for (int s = 0; s < 2; s++) {
            final int id = s;
            students[s] = new Thread(() -> {
                try { for (int k = 0; k < 10; k++) collected[id] += counter.take(); }
                catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            });
        }
        cook.start(); for (Thread t : students) t.start();
        cook.join(); for (Thread t : students) t.join();
        System.out.println("Sum of tokens collected: " + (collected[0] + collected[1]) + " (expected 210)");
    }
}
