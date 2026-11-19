import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ReportResourceDemo {
    private static List<Path> createInputs() throws IOException {
        Path dir = Files.createTempDirectory("sales-stores");
        Files.writeString(dir.resolve("store1.txt"), "100\n200\n");
        Files.writeString(dir.resolve("store2.txt"), "50\n75\n");
        Files.writeString(dir.resolve("store3.txt"), "90\nBAD-DATA\n");
        return Arrays.asList(dir.resolve("store1.txt"), dir.resolve("store2.txt"),
                             dir.resolve("store3.txt"));
    }
    private static void manual(List<Path> files, Path output) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            for (Path file : files) {
                BufferedReader reader = null;
                try {
                    reader = Files.newBufferedReader(file);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int amount = Integer.parseInt(line.trim());
                        writer.write(file.getFileName() + "," + amount);
                        writer.newLine();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Manual version: invalid data in " + file + ": " + e.getMessage());
                } finally {
                    if (reader != null) reader.close();
                }
            }
        }
    }
    private static void tryWithResources(List<Path> files, Path output) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            for (Path file : files) {
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int amount = Integer.parseInt(line.trim());
                        writer.write(file.getFileName() + "," + amount);
                        writer.newLine();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Try-with-resources: invalid data in " + file + ": " + e.getMessage());
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        List<Path> files = createInputs();
        Path manualOut = Files.createTempFile("manual-report", ".csv");
        Path safeOut = Files.createTempFile("safe-report", ".csv");
        manual(files, manualOut);
        tryWithResources(files, safeOut);
        System.out.println("Manual report lines: " + Files.readAllLines(manualOut).size());
        System.out.println("Try-with-resources report lines: " + Files.readAllLines(safeOut).size());
        System.out.println("Manual output: " + manualOut);
        System.out.println("Try-with-resources output: " + safeOut);
    }
}
