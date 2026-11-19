import java.util.*;
public class CollectionBenchmark {
    private static volatile long sink;
    private static long time(Runnable r) {
        long s = System.nanoTime(); r.run(); return System.nanoTime() - s;
    }
    private static void run(int n) {
        List<Integer> array = new ArrayList<>();
        List<Integer> linked = new LinkedList<>();
        long aAdd = time(() -> { for (int i=0;i<n;i++) array.add(i); });
        long lAdd = time(() -> { for (int i=0;i<n;i++) linked.add(i); });
        long aFront = time(() -> { for (int i=0;i<Math.min(n,2000);i++) array.add(0,i); });
        long lFront = time(() -> { for (int i=0;i<Math.min(n,2000);i++) linked.add(0,i); });
        int reps = Math.min(n, 5000);
        long aGet = time(() -> { for (int i=0;i<reps;i++) sink += array.get(i); });
        long lGet = time(() -> { for (int i=0;i<reps;i++) sink += linked.get(i); });
        long aIter = time(() -> { long x=0; for (int v:array) x+=v; sink=x; });
        long lIter = time(() -> { long x=0; for (int v:linked) x+=v; sink=x; });
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        long dqEnds = time(() -> { for(int i=0;i<n;i++) deque.addLast(i); for(int i=0;i<n/2;i++) deque.removeFirst(); for(int i=0;i<n/2;i++) deque.addFirst(i); });
        System.out.println("n="+n);
        System.out.println("append ArrayList="+aAdd+" ns, LinkedList="+lAdd+" ns");
        System.out.println("front insert (up to 2000) ArrayList="+aFront+" ns, LinkedList="+lFront+" ns");
        System.out.println("indexed get (up to 5000) ArrayList="+aGet+" ns, LinkedList="+lGet+" ns");
        System.out.println("iteration ArrayList="+aIter+" ns, LinkedList="+lIter+" ns");
        System.out.println("ArrayDeque end operations="+dqEnds+" ns");
    }
    public static void main(String[] args) {
        run(1000); run(10000); run(50000);
    }
}
