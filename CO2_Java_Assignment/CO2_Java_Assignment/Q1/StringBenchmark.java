public class StringBenchmark {
    private static volatile int sink;
    private static String[] fields(int count) {
        String[] a = new String[count];
        for (int i = 0; i < count; i++) a[i] = "FIELD" + i;
        return a;
    }
    private static long stringPlus(String[] a, int tickets) {
        long start = System.nanoTime();
        for (int t = 0; t < tickets; t++) {
            String s = "";
            for (String x : a) s += x + "|";
            sink = s.length();
        }
        return System.nanoTime() - start;
    }
    private static long builder(String[] a, int tickets) {
        long start = System.nanoTime();
        for (int t = 0; t < tickets; t++) {
            StringBuilder s = new StringBuilder();
            for (String x : a) s.append(x).append('|');
            sink = s.length();
        }
        return System.nanoTime() - start;
    }
    private static long buffer(String[] a, int tickets) {
        long start = System.nanoTime();
        for (int t = 0; t < tickets; t++) {
            StringBuffer s = new StringBuffer();
            for (String x : a) s.append(x).append('|');
            sink = s.length();
        }
        return System.nanoTime() - start;
    }
    public static void main(String[] args) {
        int[][] workloads = {{6, 1}, {50, 10000}, {100, 2000}};
        System.out.println("Fields,Tickets,String(ns),StringBuilder(ns),StringBuffer(ns)");
        for (int[] w : workloads) {
            String[] a = fields(w[0]);
            // Warm-up
            builder(a, Math.min(w[1], 100));
            long p = stringPlus(a, w[1]);
            long b = builder(a, w[1]);
            long sb = buffer(a, w[1]);
            System.out.println(w[0] + "," + w[1] + "," + p + "," + b + "," + sb);
        }
    }
}
