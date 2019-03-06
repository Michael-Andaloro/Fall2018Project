import java.io.FileReader;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

class DSGraph {

    // Undirected weighted graph
    DSHashMap<DSHashMap<Integer>> graph;

    /**
     * Constructor with no parameters.
     */
    public DSGraph(){
	graph = new DSHashMap<>();
    }

    
    /** 
     * Constructor that takes a DSHashMap<DSHashMap<Integer>>, in case one is available
     */
    public DSGraph(DSHashMap<DSHashMap<Integer>> g){
	graph = g;
    }


    /**
     * Method to add a vertex to the graph
     * Returns true if v is a new vertex, false otherwise
     */
    public boolean addVertex(String v){
	if(graph.containsKey(v))
	    return false;
	else{
	    graph.put(v, new DSHashMap<Integer>());
	    return true;
	}
    }

    /**
     * Adds an edge between two vertices.
     * If the vertices are not in the graph already, it adds them.
     * Sets the weight of the edge to the given weight.
     */
    public void addEdge(String v, String w, int weight){
	if(!graph.containsKey(v)) graph.put(v, new DSHashMap<Integer>());
	if(!graph.containsKey(w)) graph.put(w, new DSHashMap<Integer>());
	graph.get(v).put(w, weight);	// default weight is 1
	graph.get(w).put(v, weight);	// default weight is 1
    }

    /**
     * Adds an edge between two vertices.
     * If the vertices are not in the graph already, it adds them.
     * Sets the weight of the edge to 1.
     */
    public void addEdge(String v, String w){
	if(!graph.containsKey(v)) graph.put(v, new DSHashMap<Integer>());
	if(!graph.containsKey(w)) graph.put(w, new DSHashMap<Integer>());
	graph.get(v).put(w, 1);	// default weight is 1
	graph.get(w).put(v, 1);	// default weight is 1
    }

    
    /**
     * For testing
     */
    public static void main(String[] args){
	DSGraph G = new DSGraph();
	G.readGraph("GraphW.txt");
	//System.out.println(G);
	//System.out.println("Number of edges: " + G.numberOfEdges());
	G.sort();
	//G.MWST();
	System.out.println(G.MWST());
	//G.makeTree();
    }


    /**
     * Returns the number of edges in the graph
     */
    public int numberOfEdges(){
	int count = 0;
	DSArrayList<String> k = graph.getKeys();
	for(String s : k){
	    count += graph.get(s).getKeys().size();
	}

	return count/2;
    }


    /**
     * Returns the number of vertices in the graph
     */
    public int numberOfVertices(){
	return graph.getKeys().size();
    }








    /**
     * Return a graph containing a minimum-weight spanning tree of this graph.
     */
    public DSGraph MWST(){
        DSGraph tree = new DSGraph();
        DSHashMap<String> parents = new DSHashMap<>();
        for(String v: graph.getKeys()){
            parents.put(v,v);
        }
        
        Edge[] sorted_edges = sort();
        for(Edge x : sorted_edges){
            if(!findParent(parents, x.v).equals(findParent(parents, x.w))){
                parents.put(findParent(parents, x.w), findParent(parents, x.v));
                tree.addEdge(x.v, x.w, x.weight);
            }
            
        }
        return tree;
    }
    
    public Edge[] sort(){
        int counter = 0;
        DSArrayList<String> k = graph.getKeys();
        DSArrayList<Edge> edges = new DSArrayList<>();
        DSHashMap<String> visited = new DSHashMap<>();
	    for(String v : k){
	        DSArrayList<String> j = graph.get(v).getKeys();
	        for(int i = 0; i < j.size(); i++){
	            if(!visited.containsKey(v+j.get(i))){
	                counter ++;
	                Edge e = new Edge(v, j.get(i), graph.get(v).get(j.get(i)) );
	                visited.put(v+j.get(i), "v");
	                visited.put(j.get(i)+v, "v");
	                edges.add(e);
	                
	                //System.out.println("add: " + v+j.get(i) + " " + counter);
	            }
	        }
	    }
	    Edge[] a_edges = new Edge[edges.size()];
	    for(int i = 0; i < edges.size(); i ++){
	        a_edges[i] = edges.get(i);
	    }
	    Arrays.sort(a_edges, (a, b) -> a.weight - b.weight);
	    //for(Edge e : a_edges){
	      //System.out.println(e.v + ", " + e.w + ": " + e.weight);
	    //}
	    return a_edges;
    }
    
    
    

    
    //find first parent by tracing back each vertex until we find a parent that's null
    public String findParent(DSHashMap<String> parents, String a){
        while(!parents.get(a).equals(a)){
            a = parents.get(a);
        }
        return a;
    }

    
    
    
    

    
    /**
     * Print out a depiction of the graph, as so:
     * A : B:4, C:17, F:12, G:3, 
     * B : A:4, C:1, D:5, H:2, K:6, 
     * ...
     */
    public String toString(){
	String rv = "";
	for(String k : graph.getKeys()){
	    rv = rv + k + ": ";
	    for(String l : graph.get(k).getKeys()){
		rv = rv + l + " "; // + ":" + graph.get(k).get(l) + ", ";
	    }
	    rv += "\n";
	}
	return rv;
    }

	

    public void readGraph(String filename){
	//System.out.println("** Reading Graph File: " + filename);
			
	try { 
	    FileReader f = new FileReader(filename);
	    BufferedReader reader = new BufferedReader(f);
	    String line = reader.readLine();
	    int numEdges = Integer.valueOf(line.trim());
	    
	    graph = new DSHashMap<>();
	    for(int i = 0; i < numEdges; i++){
		line = reader.readLine();
		String[] parts = line.trim().split(" ");
		String v = parts[0];
		String w = parts[1];
		int weight = Integer.valueOf(parts[2]);
		if(!graph.containsKey(v)) graph.put(v, new DSHashMap<Integer>());
		if(!graph.containsKey(w)) graph.put(w, new DSHashMap<Integer>());
		graph.get(v).put(w, weight);
		graph.get(w).put(v, weight);
	    }
	    reader.close();
	} catch (IOException x) {
	    System.err.format("IOException: %s\n", x);
	}
	//System.out.println("Done Reading Graph File");
    }

    
    /**
     * fills static graph field with a graph
     */
    public void buildTestGraph(){    
	graph = new  DSHashMap<DSHashMap<Integer>>();

	graph.put("A", new DSHashMap<Integer>());
	graph.put("B", new DSHashMap<Integer>());
	graph.put("C", new DSHashMap<Integer>());
	graph.put("D", new DSHashMap<Integer>());
	graph.put("E", new DSHashMap<Integer>());

	graph.get("A").put("B", 1);
	graph.get("A").put("E", 3);

	graph.get("B").put("A", 1);
	graph.get("B").put("C", 4);
	graph.get("B").put("D", 2);

    }
    
     
	static class Edge{
	    String v;
	    String w;
	    int weight;
	    
	    public Edge(String vv, String ww, int wt){
	      v = vv;
	      w = ww;
	      weight = wt;
	    }
	}
}
