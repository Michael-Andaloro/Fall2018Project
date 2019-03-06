/**
 * DSHashMap class
 * 18 Sept. '18
 */
import java.math.BigInteger;

public class DSHashMap <T> {
  private DSArrayList <DSArrayList<KVP> > a;// backing DSArrayList
  int capacity = 5; //backing array size
  double threshold = 0.5;
  int numItems = 0;
  
  /**
   * Constructor
   */
  public DSHashMap(){
    capacity = 5;
    a = new DSArrayList<DSArrayList<KVP>>(capacity);
  }
  
  
  /**
   * Insert a new value associated with the given key, into the hash map
   */
  public void put(String key, T val){
    int hash = hash(key);
    
    // Are we the first key to hash to this location?
    if(a.get(hash) == null) // If so, put an empty chain here
      a.set(hash, new DSArrayList<KVP>());
    
    // Check to see if this key is already in the chain
    DSArrayList<KVP> akvp = a.get(hash); // reference to this key's chain
    for(int i = 0; i < akvp.size(); i++){
      if(key.equals(akvp.get(i).key)){
        akvp.get(i).value = val;
        return; // Found it. Change the value and exit
      }
    }
    
    // HOMEWORK NOTE
    // You should call the rehash() function here, whenever you need to double the capacity.
    // I have created a stub below
    
    // If we make it here, then this is a new key. So create a new
    // KVP and add it to the chain
    KVP kvp = new KVP(key, val);
    akvp.add(kvp);
    numItems++;
    if(numItems > capacity * threshold)
      rehash();
  }
  
  /**
   * Create a new backing array that's (at least) twice as large as the 
   * current one, and then place all items back into this new
   * backing array.
   */
  private void rehash() {
    // Generate a new size
    // System.out.println("Rehashing");
    BigInteger oldSize = BigInteger.valueOf(2*capacity);
    BigInteger newSize = oldSize.nextProbablePrime();
    capacity = newSize.intValue();
    
    DSArrayList<DSArrayList<KVP>> olda = a;
    a = new DSArrayList<DSArrayList<KVP>>(capacity);
    
    numItems = 0;
    for(int i = 0; i < olda.capacity(); i++){
      DSArrayList<KVP> l = olda.get(i);
      if(l == null) continue;
      for(int j = 0; j < l.size(); j++) {
        KVP kvp = l.get(j);
        put(kvp.key, kvp.value);
      }
    }
    
    olda = null;  // Let the garbage collector reclaim the old array
  }
  
  
  
  
  /**
   * Return the value associated with a given key in the hash map
   * Or null if the key is not found
   */
  public T get(String key){
    int hash = hash(key);
    DSArrayList<KVP> akvp = a.get(hash); // a direct reference to the chain we're looking at
    if(akvp == null) return null;
    
    // Loop over the chain until we find the key
    for(int i = 0; i < akvp.size(); i++)
      if(akvp.get(i).key.equals(key)) 
      return akvp.get(i).value; // Found the key. Return the value.
    
    // If we make it here, then the key was not in the hash map
    return null;
  } 
  
  
  /**
   * Return true if the given key is in the hash map, otherwise false
   * Almost the same code as get()
   */
  public boolean containsKey(String key){
    int hash = hash(key);
    DSArrayList<KVP> akvp = a.get(hash); // a direct reference to the chain we're looking at
    if(akvp == null) return false;
    
    // Loop over the chain until we find the key
    for(int i = 0; i < akvp.size(); i++)
      if(akvp.get(i).key.equals(key)) 
      return true;  // Found it!
    
    // If we make it here, then the key was not in the hash map
    return false;
  }


    /**
     * Returns a DSArrayList of all the keys in the DSHashMap
     */
    public DSArrayList<String> getKeys(){
	DSArrayList<String> rv = new DSArrayList<String>();
	
	for(DSArrayList<KVP> chain : a){
	    if(chain == null) continue;
	    for(KVP kvp : chain){
		rv.add(kvp.key);
	    }
	}

	return rv;
    }

  
  
  public void histogram(){
    DSHashMap<Integer> counts = new DSHashMap<Integer>();
    int max = 0;
    int minn = 1000000000;
    int maxval = 0;
    for(int i = 0; i < capacity; i++){
      int x = 0;
      if(a.get(i) != null)
        x = a.get(i).size();
      String key = "" + x;
      if(counts.containsKey(key)){
        counts.put(key, counts.get(key) + 1);
      }
      else{
        counts.put(key, 1);
      }
      if(x > max) max = x;
      if(x < minn) minn = x;
      if(counts.get(key) > maxval)
        maxval = counts.get(key);
    }
    
    for(int i = minn; i <= max; i++){
      if(!counts.containsKey(""+i)) continue;
      System.out.print(i + "\t");
      System.out.println(counts.get(""+i));
    }
    
  }
  
  
  /**
   * Hash function, returning the index of the chain that this key should be in.
   */
  private int hash(String s){
    int val = 0;
    int multiplier = 2;
    
    for(int i = 0; i<s.length();i++)
      val = (val * multiplier + (int)(s.charAt(i))) % capacity;
    
    return val;
  }
  
  
  /**
   * Inner calss to hold key-value pairs.
   **/
  private class KVP{
    String key;
    T value;
    
    public KVP(String key, T value){
      this.key = key;
      this.value = value;
    }
  }
  
  
}
