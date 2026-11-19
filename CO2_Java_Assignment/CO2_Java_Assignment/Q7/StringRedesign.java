public class StringRedesign {
    private static volatile int sink;
    private static long original(String[] fields,int tickets){
        long start=System.nanoTime();
        for(int t=0;t<tickets;t++){
            String result="";
            for(String f:fields) result += f + "|";
            sink=result.length();
        }
        return System.nanoTime()-start;
    }
    private static long redesigned(String[] fields,int tickets){
        long start=System.nanoTime();
        for(int t=0;t<tickets;t++){
            StringBuilder result=new StringBuilder();
            for(String f:fields) result.append(f).append('|');
            sink=result.length();
        }
        return System.nanoTime()-start;
    }
    public static void main(String[] args){
        String[] fields=new String[50];
        for(int i=0;i<fields.length;i++) fields[i]="DATA"+i;
        int tickets=10000;
        redesigned(fields,100); // warm-up
        long before=original(fields,tickets);
        long after=redesigned(fields,tickets);
        System.out.println("Workload: "+tickets+" tickets x "+fields.length+" fields");
        System.out.println("Original String concatenation: "+before+" ns");
        System.out.println("Redesigned StringBuilder: "+after+" ns");
        System.out.println("Compare results over multiple runs; timings vary by machine and JVM.");
    }
}
