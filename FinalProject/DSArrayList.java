import java.util.Iterator;

/**
 *Wrap an array in a DSArrayList class that gives us a
 * resizable array, and some other functionality
 */

/** We will make this class generic by adding a type parameter
 * to the class declaration
 */
public class DSArrayList  < T > implements Iterable < T >{
    // Backing array
    private T[] a;

    // Number of items in the array already
    private int size;

    // Number of items the array can currently hold (allocated size)
    private int capacity;

    // Constructor
    public DSArrayList(){
	this(10);
    }

    // Constructor
    public DSArrayList(int initCap){
	size = 0;
	capacity = initCap;
	// Song and dance. We create an array of Object references,
	// and then cast it to an array of type T.
	a = (T[])(new Object[capacity]);
    }

    
    /**
       Insert a new item into the array, after the last existing item.
       If the array is full, it "re-sizes" the array
    */
    public void add(T frank){
	// Check if we're full
	if(size == capacity){ // re-allocate the backing array and copy
	    int old_capacity = capacity;
	    capacity = (int)(capacity * 2);  // Make room for one more
	    T[] new_a = (T[])(new Object[capacity]);
	    for(int i = 0; i < old_capacity; i++){
		new_a[i] = a[i];
	    }
	    a = new_a;  // backing array is our new array.
	    // Old array can be garbage collected
	    //System.out.println("Resized to size " + capacity);
	}

	   
	// New item goes into the slot indexed by "size"
	a[size] = frank;
	size++;  // We keep track of the # of items in the array
	//System.out.println("Just added " + frank);
    }


    /**
     * Returns the number of items that have been stored in the array
     */
    public int size(){
	return size;
    }


    /**
     * Returns the size of the backing array
     */
    public int capacity(){
	return capacity;
    }


    /** 
     * Gets the item at index idx from the array list
     */
    public T get(int idx){
	return a[idx];
    }

    /** 
     * Change the value at location idx to new value val
     * Also make sure that "size" points to the space after the last item
     */
    public void set(int idx, T val){
	if(idx >= size)
	    size = idx + 1;
	a[idx] = val; // Will throw ArrayIndexOutOfBoundsException to an unwise user.
    }


    /**
     * Implement the iterator() method so that we can "foreach" loop over DSArrayList
     */
    public Iterator<T> iterator(){
	return new Iterator <T>(){
	    int count = 0; // The number of items we've looped over so far.
	    
	    public boolean hasNext(){
		return count < size;
		/* Equivalent to, but cooler than
		if(count < size)
		    return true;
		else
		    return false;
		*/
	    }

	    public T next(){
		count++;
		return a[count-1];
		// return a[count++];   <-- Also the cool kid way of doing it
	    }
	};

    }
}
