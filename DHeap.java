
/**
 * D-Heap
 */

public class DHeap
{
	 
	private int size, max_size, d; 
	private DHeap_Item[] array; 
    
	// Constructor
	// m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
               max_size = m_size;
			   d = m_d;
               array = new DHeap_Item[max_size];
               size = 0;
    }
	
	/**
	 * public int getSize()
	 * Returns the number of elements in the heap.
	 */
	public int getSize() {
		return size;
	}

  /**
     * public int arrayToHeap()
     *
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * preconidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * 				  size = array.length()
     * Returns number of comparisons along the function run. 
	 */
    public int arrayToHeap(DHeap_Item[] array1) {
    	if (array1.length == 0){
    		return 0; 
    	}
    	size = array1.length;
    	array = new DHeap_Item[max_size];
    	for (int i = 0; i<array1.length;i++){
    		array[i] = array1[i];
    	}
    	for (int i = 0; i<size;i++){
    		array[i].setPos(i);  //not assuming each item has the right pos
    	}
    	int count = 0;
    	int firstNonLeafChild = (int)((size-1)/d);
     	for (int i = firstNonLeafChild; i>=0; i--){
     		count = count + heapifyDown (array1[i]); 
    	}    	
        return count;
    }
    
	/**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *   
     */
    public boolean isHeap() {
    	for (int i =0; i<size;i++){
    		int numberOfChildren = numberOfChildren(array[i]);
    		if (numberOfChildren > d){
    			return false; 
    		}
    		if (numberOfChildren != 0){
   				if (minOfChildren(array[i]).getKey() < array[i].getKey()){
   					return false; 
   				}
   			}
    	}
        return true; 
    }

 /**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of 
     * vertex i in a complete D-ary tree stored in an array. 
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) {
    	if (i == 0){
    		return 0; //the item in question is the root, and have no parent. 
    	}
    	int parentPos = (int)((i-1)/d); // (i-1)/d with no decimal. 
    	return parentPos;
    }    

    public static int child (int i, int k, int d) {
    	int kthChildPos = (d*i)+k;
    	return kthChildPos;
    } 
    
    /**
    * public int Insert(DHeap_Item item)
    *
	* Inserts the given item to the heap.
	* Returns number of comparisons during the insertion.
	*
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) {
    	array[size] = item;
    	item.setPos(size);
    	size++; 
    	return heapifyUp(item);
    }

 /**
    * public int Delete_Min()
    *
	* Deletes the minimum item in the heap.
	* Returns the number of comparisons made during the deletion.
    * 
	* precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    public int Delete_Min() {
    	DHeap_Item dHeap_Item = this.array[size-1];
    	this.array[0]=dHeap_Item;
    	dHeap_Item.setPos(0);
    	this.array[size-1] = null;
    	size = size-1; 
    	return heapifyDown(dHeap_Item);
     	
    } 

    /**
     * public DHeap_Item Get_Min()
     *
	 * Returns the minimum item in the heap.
	 *
     * precondition: heapsize > 0
     *               isHeap()
     *				 size > 0
     * 
     * postcondition: isHeap()
     */
    public DHeap_Item Get_Min() {
	return array[0];
    }
	
  /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     *
	 * Decerases the key of the given item by delta.
	 * Returns number of comparisons made as a result of the decrease.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta){
    	if (delta < 0 ){ //key MUST be decreased, therefore delta must be a non-negative number
    		return 0; 
    	}
    	int key = item.getKey();
    	item.setKey(key-delta);
    	return heapifyUp(item);
    }
    
	  /**
     * public int Delete(DHeap_Item item)
     *
	 * Deletes the given item from the heap.
	 * Returns number of comparisons during the deletion.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item) {
	int currentMin  = this.Get_Min().getKey();
	int delta = item.getKey()-currentMin;
	int count = this.Decrease_Key(item, delta+1); //count = number of comparisions made in order to make the item the current minimum
    	return count + this.Delete_Min();
    }
	
	/**
	* Sort the input array using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, name of the items is irrelevant.
	* 
	* Returns the number of comparisons performed.
	* 
	* postcondition: array1 is sorted 
	*/
	public static int DHeapSort(int[] array1, int d) {
		DHeap_Item[] DHArray=new DHeap_Item[array1.length]; 
		for(int i=0;i<array1.length;i++){ 
			DHArray[i]=new DHeap_Item(""+array1[i],array1[i]);
		} 
		int count=0;
		DHeap dHeap=new DHeap(d,array1.length);
		count += dHeap.arrayToHeap(DHArray);
		for(int j=0;j<array1.length;j++){
			array1[j]=dHeap.Get_Min().getKey();
			count += dHeap.Delete_Min();
		}
		return count;
	}
	
	/**
	* returns the number of children of the given item
	*  
	* precondition: item.pos < size;
    *               item != null
	*
	*/
	public int numberOfChildren (DHeap_Item item){
    	int i = item.getPos();
    	int numberOfChildren = 0;
    	for (int j = child(i,1,d); j<=child(i,d,d); j++){
    		if (j<size) { 
    			numberOfChildren++;
    		}
    	}
		return numberOfChildren;
	} 

	/**
	* returns the minimal child of the given item  
	*  
	* precondition: item.pos < size;
    *               item != null
	*
	*/
	public DHeap_Item minOfChildren (DHeap_Item item){
		int numberOfChildren = numberOfChildren(item);
		int position = item.getPos();
		DHeap_Item minChild = array[child(position,1,d)];
		for (int k = 2; k<=numberOfChildren;k++){
			DHeap_Item tempChild = array[child(position,k,d)];
			if (tempChild.getKey() < minChild.getKey()){
				minChild = tempChild;
			}
		}
		return minChild;
	}
	
    public int heapifyDown(DHeap_Item item) {
    	int count = 0;
    	int numberOfChildren = numberOfChildren(item);
    	if (numberOfChildren == 0){
    		return 0; 
    	}
    	DHeap_Item minChild = minOfChildren(item);
    	if (minChild.getKey() < item.getKey()){ //switching between them
    		
    			int dHeapPosition = item.getPos();
    			int minChildPoistion = minChild.getPos();
    			
    			array[minChildPoistion] = item; 
    			array[dHeapPosition] = minChild;
    			
    			item.setPos(minChildPoistion);
    			minChild.setPos(dHeapPosition);
    			
        		return count + numberOfChildren + heapifyDown(item); 
    		}    		
    	return count + numberOfChildren; 
    	// did one comparison with the minimum, and in order to find
    	// the minimum we had to do a number of comparison = number of children - 1.   
    }
    
    public int heapifyUp(DHeap_Item item) {
    	int count = 0;
    	if (item.getPos() == 0){ //the given item is the root
    		return 0; 
    	}
    	
    	int parentPos = parent(item.getPos(), d);
    	DHeap_Item parent = array[parentPos];
    	
    	if (item.getKey() < parent.getKey()){ //the child is smaller than the parent, need to switch 
    		
    		int parentPosition = parent.getPos();
    		int dHeapPosition = item.getPos();

    		array[dHeapPosition] = parent;
    		array[parentPosition] = item; 
    		
    		item.setPos(parentPosition);
    		parent.setPos(dHeapPosition);
    		    		
    		return count + 1 + heapifyUp(item);
    	}
    	return count + 1; 
    }
    
}
