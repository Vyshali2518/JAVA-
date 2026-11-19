import java.util.*;
public class PharmacyInventory {
    private static volatile int sink;
    public static void main(String[] args) {
        for (int n : new int[]{1000, 10000, 50000}) run(n);
    }
    private static void run(int n) {
        Map<String,Integer> hash = new HashMap<>();
        Map<String,Integer> tree = new TreeMap<>();
        Map<String,Integer> linked = new LinkedHashMap<>();
        long hPut=fill(hash,n), tPut=fill(tree,n), lPut=fill(linked,n);
        String[] keys = new String[n];
        for(int i=0;i<n;i++) keys[i]=String.format("MED%06d",i);
        long hGet=lookup(hash,keys), tGet=lookup(tree,keys), lGet=lookup(linked,keys);
        long hIter=iterate(hash), tIter=iterate(tree), lIter=iterate(linked);
        System.out.println("\\nInventory size: "+n);
        System.out.println("put ns HashMap="+hPut+", TreeMap="+tPut+", LinkedHashMap="+lPut);
        System.out.println("lookup ns HashMap="+hGet+", TreeMap="+tGet+", LinkedHashMap="+lGet);
        System.out.println("iteration ns HashMap="+hIter+", TreeMap="+tIter+", LinkedHashMap="+lIter);
        if(n==1000) {
            System.out.println("Code-order report (TreeMap): "+new ArrayList<>(tree.keySet()).subList(0,Math.min(5,n)));
            System.out.println("Insertion-order report (LinkedHashMap): "+new ArrayList<>(linked.keySet()).subList(0,Math.min(5,n)));
        }
    }
    private static long fill(Map<String,Integer> m,int n) {
        long s=System.nanoTime();
        for(int i=0;i<n;i++) m.put(String.format("MED%06d",i),i);
        return System.nanoTime()-s;
    }
    private static long lookup(Map<String,Integer> m,String[] keys) {
        long s=System.nanoTime(); int x=0;
        for(String k:keys) x+=m.get(k);
        sink=x; return System.nanoTime()-s;
    }
    private static long iterate(Map<String,Integer> m) {
        long s=System.nanoTime(); int x=0;
        for(int v:m.values()) x+=v;
        sink=x; return System.nanoTime()-s;
    }
}
