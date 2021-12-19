/**
 * FibonacciHeap
 *
 * An implementation of a Fibonacci Heap over integers.
 */
public class FibonacciHeap
{

    private int size;
    private int trees;
    private static int links=0;
    private static int cuts=0;
    private int marked;
    private HeapNode first;
    private HeapNode min;

    public FibonacciHeap() {
        this.size = 0;
        this.trees = 0;
        this.marked = 0;
        this.first = null;
        this.min = null;
    }

   /**
    * public boolean isEmpty()
    *
    * Returns true if and only if the heap is empty.
    *
    * Complexity: O(1)
    */
    public boolean isEmpty()
    {
    	return this.size == 0;
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * The added key is assumed not to already belong to the heap.  
    * 
    * Returns the newly created node.
    * Complexity: O(1)
    */
    public HeapNode insert(int key)
    {    
    	HeapNode node=new HeapNode(key);
    	if(!this.isEmpty()) {
    	    node.setPrev(this.first.getPrev());
            this.first.getPrev().setNext(node);

            node.setNext(this.first);
            this.first.setPrev(node);

            this.first=node;
        }
    	else
    	    this.first=node;
    	if(this.min==null){
    	    this.min=node;
        }
    	else{
    	    if(this.min.getKey()>key)
    	        this.min=node;
        }
        this.size++;
        this.trees++;
    	return node;
    }

   /**
    * public void deleteMin()
    *
    * Deletes the node containing the minimum key.
    *
    * Complexity: O(log n)
    */
    public void deleteMin() {
        if (!this.isEmpty())
        {
            if (this.min.getChild() != null)
            {
                if(trees==1)
                {
                    this.first=this.min.getChild();
                }
                else{
                    // remove the min heap node
                    HeapNode firstNodeMin = this.min.getChild();
                    HeapNode lastNodeMin = this.min.getChild().getPrev();
                    HeapNode prevMin = this.min.getPrev();
                    HeapNode nextMin = this.min.getNext();
                    HeapNode originalLast = this.first.getPrev();

                    // if min is the first- connect right child to min's next,
                    // make min's child the first and connect to the end
                    if (this.first == this.min) {
                        this.first = firstNodeMin;
                        this.first.setPrev(originalLast);
                        originalLast.setNext(this.first);
                        lastNodeMin.setNext(nextMin);
                        nextMin.setPrev(lastNodeMin);
                    }
                    // if min is the last- connect min's child to min's prev,
                    // make right child of min the last and connect to the first
                    else if(this.min==originalLast){
                        this.first.setPrev(lastNodeMin);
                        lastNodeMin.setNext(this.first);
                        prevMin.setNext(firstNodeMin);
                        firstNodeMin.setPrev(prevMin);
                    }
                    // put the trees in the middle- no special cases
                    else{
                        prevMin.setNext(firstNodeMin);
                        firstNodeMin.setPrev(prevMin);
                        nextMin.setPrev(lastNodeMin);
                        lastNodeMin.setNext(nextMin);
                    }
                    // remove marks from min's childs
                    HeapNode node = firstNodeMin;
                    while (node.getNext() != lastNodeMin) {
                        node.setMarked(false);
                        node = node.getNext();
                    }
                }
            }
            else {
                // no child- min is a single node
                // one node in the heap
                if (this.min == this.first && this.size==1) {
                    this.first = null;
                    this.min=null;
                }
                else{
                    if (this.min == this.first)
                        this.first=this.min.getNext();
                    this.min.getPrev().setNext(this.min.getNext());
                    this.min.getNext().setPrev(this.min.getPrev());
                }
            }
            this.size = this.size - 1;


            if (this.first != null)
            {
                this.min.setPrev(null);
                this.min.setNext(null);

                boolean run=true;
                HeapNode[] arr = new HeapNode[(int)(Math.log(this.size) / Math.log(2))+1];
                HeapNode node = this.first;

                // make loop on first and on every node until we will be in first again
                while (node!= this.first || run) {
                    run=false;
                    int rank = node.getRank();
                    HeapNode nextNode = node.getNext();

                    HeapNode mergeNode = node;
                    fixNode(mergeNode);
                    while (arr[rank] != null) {
                        if (mergeNode.getKey() < arr[rank].getKey()) {
                            connect(mergeNode, arr[rank]);
                            links++;
                        } else {
                            connect(arr[rank], mergeNode);
                            mergeNode = arr[rank];
                            links++;
                        }
                        arr[rank]=null;
                        rank++;
                    }
                    arr[rank]=mergeNode;
                    node = nextNode;
                }

                boolean first = true;
                int leftIndex = -1;
                this.trees = 0;
                // connect all merged nodes
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] != null) {
                        this.trees++;
                        arr[i].setMarked(false);
                        if (first) {
                            this.first = arr[i];
                            first = false;
                            this.min = arr[i];
                            leftIndex = i;
                        }
                        else {
                            // updating min
                            if (this.min.getKey() > arr[i].getKey())
                                this.min = arr[i];

                            // connect to left node
                            HeapNode leftNode = arr[leftIndex];

                            leftNode.setNext(arr[i]);
                            arr[i].setPrev(leftNode);

                            this.first.setPrev(arr[i]);
                            leftIndex = i;
                        }
                    }
                }
                this.first.getPrev().setNext(this.first);
            }
        }
    }

   /**
    * public HeapNode findMin()
    *
    * Returns the node of the heap whose key is minimal, or null if the heap is empty.
    *
    * Complexity: O(1)
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
   /**
    * public void meld (FibonacciHeap heap2)
    *
    * Melds heap2 with the current heap.
    * Complexity: O(1)
    *
    */
    public void meld (FibonacciHeap heap2)
    {
    	  return; // should be replaced by student code   		
    }

   /**
    * public int size()
    *
    * Returns the number of elements in the heap.
    * Complexity: O(1)
    *   
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
    * public int[] countersRep()
    *
    * Return an array of counters. The i-th entry contains the number of trees of order i in the heap.
    * Note: The size of of the array depends on the maximum order of a tree, and an empty heap returns an empty array.
    *
     * Complexity:
    */
    public int[] countersRep()
    {
    	int[] arr = new int[100];
        return arr; //	 to be replaced by student code
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap.
	* It is assumed that x indeed belongs to the heap.
    *
    * Complexity:O(log n)
    */
    public void delete(HeapNode x) 
    {    
    	int min=this.min.getKey();
    	this.decreaseKey(x,x.getKey()+1-min);
    	deleteMin();
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * Decreases the key of the node x by a non-negative value delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    *
    * Complexity:O()
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	return; // should be replaced by student code
    }

   /**
    * public int potential() 
    *
    * This function returns the current potential of the heap, which is:
    * Potential = #trees + 2*#marked
    * 
    * In words: The potential equals to the number of trees in the heap
    * plus twice the number of marked nodes in the heap.
    *
    * Complexity:O(1)
    */
    public int potential() 
    {    
    	return this.trees + 2 * this.marked;
    }

   /**
    * public static int totalLinks() 
    *
    * This static function returns the total number of link operations made during the
    * run-time of the program. A link operation is the operation which gets as input two
    * trees of the same rank, and generates a tree of rank bigger by one, by hanging the
    * tree which has larger value in its root under the other tree.
    *
    * Complexity:O(1)
    */
    public static int totalLinks()
    {    
    	return links;
    }

   /**
    * public static int totalCuts() 
    *
    * This static function returns the total number of cut operations made during the
    * run-time of the program. A cut operation is the operation which disconnects a subtree
    * from its parent (during decreaseKey/delete methods).
    *
    * Complexity:O(1)
    */
    public static int totalCuts()
    {    
    	return cuts;
    }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k smallest elements in a Fibonacci heap that contains a single tree.
    * The function should run in O(k*deg(H)). (deg(H) is the degree of the only tree in H.)
    *  
    * ###CRITICAL### : you are NOT allowed to change H. 
    */
     public static int[] kMin(FibonacciHeap H, int k)
     {
         int[] arr = new int[k];
         HeapNode minH=H.findMin();

         int count=0;
         FibonacciHeap heap=new FibonacciHeap();

         HeapNode insertedNode=heap.insert(minH.getKey());
         insertedNode.setPtrNode(minH);

         // add the k smallest elements-after the minimum is already added
         while(count!=k){
             // the minimum in the new heap
             minH=heap.findMin();
             // add the minimum element to the array
             arr[count]=minH.getKey();
             // remove from the temporary heap
             heap.deleteMin();
             count++;

             // if the minimum element doesn't have child' we will keep serching
             // for the next smallest element in the heap until this element has child
             while(minH.getPtrNode().getChild()==null && count<=k-1){
                 minH=heap.findMin();
                 arr[count]=minH.getKey();
                 heap.deleteMin();
                 count++;
             }

             if(count==k){
                 break;
             }

             // add the children of the minimum to the temporary heap
             boolean run=true;
             HeapNode ptr=minH.getPtrNode().getChild();
             while(ptr!=minH.getPtrNode().getChild() || run==true){
                 run=false;
                 insertedNode=heap.insert(ptr.getKey());
                 insertedNode.setPtrNode(ptr);
                 ptr=ptr.getNext();
             }
         }
         return arr;
     }

    /**
     * connect(HeapNode parent,HeapNode child)
     *
     * linking two trees- one is designated to be the parent of the other.
     * Complexity: O(1)
     */
    private void connect(HeapNode parent,HeapNode child){
        HeapNode savedChild=parent.getChild();

        parent.setChild(child);
        child.setParent(parent);

        parent.setNext(parent);
        parent.setPrev(parent);

        // connect children
        if(savedChild!=null){
            child.setPrev(savedChild.getPrev());
            savedChild.getPrev().setNext(child);

            savedChild.setPrev(child);
            child.setNext(savedChild);
        }
        else{
            child.setNext(child);
            child.setPrev(child);
        }

        parent.setRank(parent.getRank()+1);
    }

    /**
     * fixNode(HeapNode node)
     *
     * separate the node from its heap
     *
     * Complexity: O(1)
     */
    private void fixNode(HeapNode node) {
        node.setNext(node);
        node.setPrev(node);
        node.setParent(null);
    }



    // ======================delete after
    public HeapNode getFirst(){
        return this.first;
    }

   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in another file. 
    *  
    */
    public static class HeapNode{

    	private int key;
        private boolean marked;
        private int rank;
        private HeapNode child;
        private HeapNode next;
        private HeapNode prev;
        private HeapNode parent;
        private HeapNode ptrNode;

       /**
        * public HeapNode(int key)
        *
        * Constructor of a node- Tree degree 0- rank=0.
        * Set the correct key, marked- false, no child, no parent, next or prev
        *
        * Complexity - O(1)
        */
       public HeapNode(int key) {
           this.key = key;
           this.marked = false;
           this.rank = 0;
           this.child = null;
           this.next = this;
           this.prev = this;
           this.parent = null;
           this.ptrNode=null;
       }

       /**
        * public int getKey()
        * Returns node's key.
        * Complexity --O(1)
        */
    	public int getKey() {
    		return this.key;
    	}

       /**
        * public boolean getMarked()
        * Returns true- if node is marked, false- if not.
        * Complexity --O(1)
        */
        public boolean getMarked() {
            return this.marked;
        }

       /**
        * public void setMarked(boolean mark)
        * Sets marked value.
        * Complexity --O(1)
        */
        public void setMarked(boolean mark) {
            this.marked = mark;
        }

       /**
        * public int getRank()
        * Returns node's rank
        * Complexity --O(1)
        */
        public int getRank() {
            return this.rank;
        }

       /**
        * public void setRank(int rank)
        * Sets node's rank
        * Complexity --O(1)
        */
        public void setRank(int rank) {
           this.rank = rank;
       }

       /**
        * public HeapNode getChild()
        * Returns node's child. null- if there is no son
        * Complexity --O(1)
        */
        public HeapNode getChild() {
           return this.child;
       }

       /**
        * public void setChild(HeapNode node)
        * Sets node's child
        * Complexity --O(1)
        */
        public void setChild(HeapNode node) {
           this.child = node;
       }

       /**
        * public HeapNode getNext()
        * Returns the next node after the node.
        * Complexity --O(1)
        */
        public HeapNode getNext() {
           return this.next;
       }

       /**
        * public void setNext(HeapNode node)
        * Sets the next node after the node.
        * Complexity --O(1)
        */
        public void setNext(HeapNode node) {
    	    this.next = node;
       }

       /**
        * public HeapNode getPrev()
        * Returns the previous node before the node.
        * Complexity --O(1)
        */
        public HeapNode getPrev() {
           return this.prev;
       }

       /**
        * public void setPrev(HeapNode node)
        * Sets the previous node before the node.
        * Complexity --O(1)
        */
       public void setPrev(HeapNode node) {
           this.prev = node;
       }

       /**
        * public HeapNode getParent()
        * Returns the node's parent- null if the node has no parent.
        * Complexity --O(1)
        */
       public HeapNode getParent() {
           return this.parent;
       }

       /**
        * public void setParent(HeapNode node)
        * Sets the node's parent
        * Complexity --O(1)
        */
       public void setParent(HeapNode node) {
           this.parent = node;
       }

       /**
        * public void setPtrNode(HeapNode node)
        * Sets the node's ptrNode
        * Complexity --O(1)
        */
       public void setPtrNode(HeapNode node) {
           this.ptrNode = node;
       }

       /**
        * public HeapNode getPtrNode()
        * Returns the node's ptrNode
        * Complexity --O(1)
        */
       public HeapNode getPtrNode() {
           return this.ptrNode;
       }
    }
}
