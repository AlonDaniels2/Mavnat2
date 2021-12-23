import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;


public class HeapPrinter {
    static final PrintStream stream = System.out;
    static void printIndentPrefix(ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        for (int i = 0; i < size - 1; ++i) {
            stream.format("%c   ", hasNexts.get(i).booleanValue() ? '│' : ' ');
        }
    }

    static void printIndent(FibonacciHeap.HeapNode heapNode, ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        printIndentPrefix(hasNexts);

        stream.format("%c── %s\n",
                hasNexts.get(size - 1) ? '├' : '╰',
                heapNode == null ? "(null)" : String.valueOf(heapNode.getKey())
        );
    }

    static String repeatString(String s,int count){
        StringBuilder r = new StringBuilder();
        for (int i = 0; i < count; i++) {
            r.append(s);
        }
        return r.toString();
    }

    static void printIndentVerbose(FibonacciHeap.HeapNode heapNode, ArrayList<Boolean> hasNexts) {
        int size = hasNexts.size();
        if (heapNode == null) {
            printIndentPrefix(hasNexts);
            stream.format("%c── %s\n", hasNexts.get(size - 1) ? '├' : '╰', "(null)");
            return;
        }

        Function<Supplier<FibonacciHeap.HeapNode>, String> keyify = (f) -> {
            FibonacciHeap.HeapNode node = f.get();
            return node == null ? "(null)" : String.valueOf(node.getKey());
        };
        String title  = String.format(" Key: %d ", heapNode.getKey());
        List<String> content =  Arrays.asList(
                String.format(" Rank: %d ", heapNode.getRank()),
                String.format(" Marked: %b ", heapNode.isMarked()),
                String.format(" Parent: %s ", keyify.apply(heapNode::getParent)),
                String.format(" Next: %s ", keyify.apply(heapNode::getNext)),
                String.format(" Prev: %s ", keyify.apply(heapNode::getPrev)),
                String.format(" Child: %s", keyify.apply(heapNode::getChild))
        );

        /* Print details in box */
        int length = Math.max(
                title.length(),
                content.stream().map(String::length).max(Integer::compareTo).get()
        );
        String line = repeatString("─", length);
        String padded = String.format("%%-%ds", length);
        boolean hasNext = hasNexts.get(size - 1);

        //print header row
        printIndentPrefix(hasNexts);
        stream.format("%c── ╭%s╮%n", hasNext ? '├' : '╰', line);

        //print title row
        printIndentPrefix(hasNexts);
        stream.format("%c   │" + padded + "│%n", hasNext ? '│' : ' ', title);

        // print separator
        printIndentPrefix(hasNexts);
        stream.format("%c   ├%s┤%n", hasNext ? '│' : ' ', line);

        // print content
        for (String data : content) {
            printIndentPrefix(hasNexts);
            stream.format("%c   │" + padded + "│%n", hasNext ? '│' : ' ', data);
        }

        // print footer
        printIndentPrefix(hasNexts);
        stream.format("%c   ╰%s╯%n", hasNext ? '│' : ' ', line);
    }

    static void printHeapNode(FibonacciHeap.HeapNode heapNode, FibonacciHeap.HeapNode until, ArrayList<Boolean> hasNexts, boolean verbose) {
        if (heapNode == null || heapNode == until) {
            return;
        }
        hasNexts.set(
                hasNexts.size() - 1,
                heapNode.getNext() != null && heapNode.getNext() != heapNode && heapNode.getNext() != until
        );
        if (verbose) {
            printIndentVerbose(heapNode, hasNexts);
        } else {
            printIndent(heapNode, hasNexts);
        }

        hasNexts.add(false);
        printHeapNode(heapNode.getChild(), null, hasNexts, verbose);
        hasNexts.remove(hasNexts.size() - 1);

        until = until == null ? heapNode : until;
        printHeapNode(heapNode.getNext(), until, hasNexts, verbose);
    }

    public static void print(FibonacciHeap heap, boolean verbose) {
        if (heap == null) {
            stream.println("(null)");
            return;
        } else if (heap.isEmpty()) {
            stream.println("(empty)");
            return;
        }

        stream.println("╮");
        ArrayList<Boolean> list = new ArrayList<>();
        list.add(false);
        printHeapNode(heap.getFirst(), null, list, verbose);
    }

    public static void demo() {
        /* Build an example */
        FibonacciHeap heap = new FibonacciHeap();

        heap.insert(20);
        heap.insert(8);
        heap.insert(3);
        heap.insert(100);
        heap.insert(15);
        heap.insert(18);
        heap.insert(1);
        heap.insert(2);
        heap.insert(7);
        heap.deleteMin();
        heap.insert(500);

        /* Print */
        stream.println("Printing in verbose mode:");
        HeapPrinter.print(heap, true);

        stream.println("Printing in regular mode:");
        HeapPrinter.print(heap, false);
    }

    public static void testKmin(){
        FibonacciHeap fibonacciHeap = new FibonacciHeap();
        FibonacciHeap.HeapNode x=null;
        FibonacciHeap.HeapNode[] nodes=new FibonacciHeap.HeapNode[(int)Math.pow(2, 6)];
        for (int i = 0; i < Math.pow(2, 6); i++) {
            nodes[i]=fibonacciHeap.insert(i);
        }
        System.out.println("c");
        fibonacciHeap.deleteMin();
        System.out.println("min= "+fibonacciHeap.getMin().getKey());
        fibonacciHeap.deleteMin();
        System.out.println("min= "+fibonacciHeap.getMin().getKey());
        System.out.println("del key= "+nodes[35].getKey());
        fibonacciHeap.delete(nodes[35]);
        print(fibonacciHeap,false);
        for(int i=2;i<32;i++){
            if(i!=35)
                fibonacciHeap.delete(nodes[i]);
        }
        print(fibonacciHeap,false);
        int[] arr=FibonacciHeap.kMin(fibonacciHeap,25);
        System.out.println(Arrays.toString(arr));
    }

    public static void manyTesters(){
        FibonacciHeap tryr=new FibonacciHeap();
        tryr.insert(7);
        print(tryr,false);
        tryr.deleteMin();
        print(tryr,false);
        // demo();
        FibonacciHeap.HeapNode[] arrN=new FibonacciHeap.HeapNode[100];
        FibonacciHeap heap=new FibonacciHeap();
        for(int i=1;i<=100;i++){
            arrN[i-1]=heap.insert(i);
        }
        heap.deleteMin();
        heap.deleteMin();
        print(heap,false);
        System.out.println(FibonacciHeap.totalLinks());
        for(int i=3;i<=36;i++){
            //heap.deleteMin();
            heap.delete(arrN[i-1]);
        }
        print(heap,false);


        for(int i=1;i<= heap.size();i++){
            int[] arr=FibonacciHeap.kMin(heap,i);
            //System.out.println(Arrays.toString(arr));
            for(int j=0;j<i;j++){
                if(arr[j]!=j+37)
                    System.out.println("ERROR");
            }
        }
        int[] arr=FibonacciHeap.kMin(heap,2);
        System.out.println(Arrays.toString(arr));
        int result = (int)(Math.log(8) / Math.log(2));
        System.out.println(result);

        FibonacciHeap fibHeap = new FibonacciHeap();
        int size = (int) Math.pow(2, 4) + 1;
        FibonacciHeap.HeapNode[] nodes = new FibonacciHeap.HeapNode[size];
        for (int index = 0; index < size; index++) {
            nodes[index] = fibHeap.insert(index);
        }
        System.out.println(fibHeap.getMin().getKey());
        System.out.println(fibHeap.size());
        fibHeap.deleteMin();
        System.out.println(fibHeap.getFirst().getKey());
        //System.out.println(Arrays.toString(nodes));
        //System.out.println(fibHeap.getMin().getKey());
        //System.out.println(fibHeap.size());
        //if(fibHeap.potential() != 1) System.out.println("error in test 9");
        print(fibHeap,false);
        fibHeap.delete(nodes[10]);
        print(fibHeap,false);
        print(fibHeap,true);
        //print(fibHeap,false);
        //fibHeap.delete(nodes[14]);
        //fibHeap.delete(nodes[15]);
        //if(fibHeap.potential() != 2) System.out.println("error in test 9");
    }

    public static void checkDel(){
        int size=((int)Math.pow(2,3)+1);
        FibonacciHeap heap=new FibonacciHeap();
        FibonacciHeap.HeapNode[] arrNodes=new FibonacciHeap.HeapNode[size];
        for(int i=0;i<size;i++){
            arrNodes[i]=heap.insert(i+1);
        }
        print(heap,false);
        int[] countereps=heap.countersRep();
        System.out.println("countereps= "+Arrays.toString(countereps));
        heap.decreaseKey(arrNodes[5],6);
        print(heap,false);
        countereps=heap.countersRep();
        System.out.println("countereps= "+Arrays.toString(countereps));
        heap.deleteMin();
        print(heap,false);
        countereps=heap.countersRep();
        System.out.println("countereps= "+Arrays.toString(countereps));
        heap.deleteMin();
        print(heap,false);
        countereps=heap.countersRep();
        System.out.println("countereps= "+Arrays.toString(countereps));
        heap.decreaseKey(arrNodes[3],4);
        print(heap,false);
        countereps=heap.countersRep();
        System.out.println("countereps= "+Arrays.toString(countereps));

    }

    public static void main(String[] args) {
        //manyTesters();
    }

    public static void q2(int i){
        int m=(int)Math.pow(3,i)-1;
        FibonacciHeap heap=new FibonacciHeap();
        FibonacciHeap.HeapNode[] nodes=new FibonacciHeap.HeapNode[m];
        for(int k=0;k<=m;k++){
            heap.insert(k);
        }
        for(int j=1;j<=(3*m/4);j++){

        }
    }
}