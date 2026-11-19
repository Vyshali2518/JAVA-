import java.io.*;
import java.nio.file.*;

public class LineCounter {
    public static long countUnbuffered(Path path) throws IOException {
        long lines = 0;
        try (InputStream in = new FileInputStream(path.toFile())) {
            int value;
            while ((value = in.read()) != -1) if (value == '\n') lines++;
        }
        return lines;
    }

    public static long countBuffered(Path path) throws IOException {
        long lines = 0;
        byte[] buffer = new byte[64 * 1024];
        try (InputStream in = new BufferedInputStream(Files.newInputStream(path), buffer.length)) {
            int count;
            while ((count = in.read(buffer)) != -1) {
                for (int i = 0; i < count; i++) if (buffer[i] == '\n') lines++;
            }
        }
        return lines;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java LineCounter <file-path>");
            return;
        }
        Path file = Paths.get(args[0]);
        long start = System.nanoTime();
        long a = countUnbuffered(file);
        long unbuffered = System.nanoTime() - start;
        start = System.nanoTime();
        long b = countBuffered(file);
        long buffered = System.nanoTime() - start;
        System.out.println("Unbuffered lines: " + a + " | time ms: " + unbuffered / 1_000_000.0);
        System.out.println("Buffered lines:   " + b + " | time ms: " + buffered / 1_000_000.0);
    }
}
