import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

class ProcessResult {
    final String file;
    final Integer score;
    final String error;
    ProcessResult(String file, Integer score, String error) {
        this.file=file; this.score=score; this.error=error;
    }
    boolean success() { return error == null; }
}
class ScoreTask implements Callable<ProcessResult> {
    private final Path file;
    ScoreTask(Path file) { this.file=file; }
    @Override public ProcessResult call() {
        try {
            String value = Files.readString(file).trim();
            int score = Integer.parseInt(value);
            if (score < 0 || score > 100) throw new IllegalArgumentException("Score outside 0..100");
            return new ProcessResult(file.getFileName().toString(), score, null);
        } catch (IOException | IllegalArgumentException e) {
            return new ProcessResult(file.getFileName().toString(), null, e.getMessage());
        }
    }
}
public class ResultProcessor {
    private static List<Path> makeSampleFiles() throws IOException {
        Path dir = Files.createTempDirectory("exam-scores");
        for (int i=1; i<=18; i++) {
            String content = (i % 7 == 0) ? "CORRUPT" : String.valueOf((i * 5) % 101);
            Files.writeString(dir.resolve("score" + i + ".txt"), content);
        }
        // Include one missing path in the task list.
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path p : stream) files.add(p);
        }
        files.add(dir.resolve("missing-score.txt"));
        return files;
    }
    public static void main(String[] args) throws Exception {
        int poolSize = args.length > 0 ? Integer.parseInt(args[0]) : 4;
        if (poolSize < 1) throw new IllegalArgumentException("Pool size must be positive");
        List<Path> files = makeSampleFiles();
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        long start = System.nanoTime();
        List<Future<ProcessResult>> futures = new ArrayList<>();
        for (Path file : files) futures.add(pool.submit(new ScoreTask(file)));

        List<ProcessResult> results = new CopyOnWriteArrayList<>();
        for (Future<ProcessResult> future : futures) {
            try { results.add(future.get()); }
            catch (ExecutionException e) {
                results.add(new ProcessResult("unknown", null, e.getCause().toString()));
            }
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        long elapsed = System.nanoTime() - start;

        int successes=0, failures=0, total=0;
        for (ProcessResult r : results) {
            if (r.success()) { successes++; total += r.score; }
            else { failures++; System.out.println("FAILED " + r.file + ": " + r.error); }
        }
        System.out.println("Pool size: " + poolSize);
        System.out.println("Files processed: " + results.size());
        System.out.println("Successes: " + successes + ", failures: " + failures);
        System.out.println("Total of valid scores: " + total);
        System.out.println("Elapsed time (ns): " + elapsed);
    }
}
