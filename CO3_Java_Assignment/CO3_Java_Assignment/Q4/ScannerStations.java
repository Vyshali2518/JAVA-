class StationThread extends Thread {
    private final int scripts;
    StationThread(String name, int scripts) { super(name); this.scripts = scripts; }
    @Override public void run() {
        try { Thread.sleep(100 + (long)(Math.random() * 300)); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        System.out.println(getName() + " scanned " + scripts + " scripts");
    }
}
class StationTask implements Runnable {
    private final String name; private final int scripts;
    StationTask(String name, int scripts) { this.name=name; this.scripts=scripts; }
    @Override public void run() {
        try { Thread.sleep(100 + (long)(Math.random() * 300)); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
        System.out.println(name + " scanned " + scripts + " scripts");
    }
}
public class ScannerStations {
    public static void main(String[] args) throws InterruptedException {
        Thread[] stations = {
            new StationThread("Station-1", 25),
            new Thread(new StationTask("Station-2", 30), "Station-2"),
            new StationThread("Station-3", 20),
            new Thread(new StationTask("Station-4", 35), "Station-4")
        };
        for (Thread station : stations) station.start();
        for (Thread station : stations) System.out.println(station.getName() + " alive=" + station.isAlive());
        for (Thread station : stations) station.join();
        System.out.println("All scanner stations completed.");
    }
}
