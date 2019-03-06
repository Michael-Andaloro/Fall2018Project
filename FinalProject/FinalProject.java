import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Object;
import java.util.Random;
import javax.swing.BoxLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.*;
import java.awt.*;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;
import java.io.*;
import java.util.Scanner;
import java.lang.Thread;
import java.awt.Component;


public class FinalProject extends JFrame implements ActionListener{
    
    public static ArrayList<Integer> verts = new ArrayList<Integer>();
    public static HashMap<Integer, ArrayList<Integer>> vertCoords = new HashMap<Integer, ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> edges = new ArrayList<ArrayList<Integer>>();
    public static HashMap<String, Integer> weights = new HashMap<String, Integer>();
    public static ArrayList<String> fileRead = new ArrayList<String>();
    public static HashMap<Integer, Boolean> vertical = new HashMap<Integer, Boolean>();
    public static HashMap<Integer, Boolean> horizontal = new HashMap<Integer, Boolean>();
    public static HashMap<Integer, Integer> speed = new HashMap<Integer, Integer>();
    public static ArrayList<ArrayList<Integer>> MWSTEdges = new ArrayList<ArrayList<Integer>>();
    public static HashMap<String, Boolean> treeCheck = new HashMap<String, Boolean>();

    //mouse stuff
    static MouseHandler clicks;
    


    
    public static ArrayList<Integer> createCoords(){
        ArrayList<Integer> coords = new ArrayList<Integer>();
        coords.add((int)Math.floor(Math.random()*800));
        coords.add((int)Math.floor(Math.random()*600));
        return coords;
    }
    
    public static ArrayList<Integer> moveCoords(int vert, int x, int y){
        ArrayList<Integer> coords = new ArrayList<Integer>();
        if( x < 2 ){
            horizontal.put(vert, false);
        }
        if( x > 770 ){
            horizontal.put(vert, true);
        }
        if( y < 2 ){
            vertical.put(vert, false);
        }
        if( y > 570 ){
            vertical.put(vert, true);
        }
        
        if( horizontal.get(vert) ){
            coords.add(x-speed.get(vert));
        }
        else coords.add(x+speed.get(vert));
        if( vertical.get(vert) ){
            coords.add(y-speed.get(vert));
        }
        else coords.add(y+speed.get(vert));;
        return coords;
    }
    
    public static ArrayList<Integer> createEdge(int a, int b){
        ArrayList<Integer> edge = new ArrayList<Integer>();
        edge.add(a);
        edge.add(b);
        int weight = (int)(1+Math.floor(Math.random()*14));
        weights.put(String.valueOf(a)+String.valueOf(b), weight);
        weights.put(String.valueOf(b)+String.valueOf(a), weight);
        return edge;
    }

    public static ArrayList<Integer> createMWSTEdge(int a, int b){
        ArrayList<Integer> edge = new ArrayList<Integer>();
        edge.add(a);
        edge.add(b);
        return edge;
    }


    //makes text file for graph (edge and vertices only)
    //+generates graph
    public static void generate(int V){
        int E = (V*(V-1))/2;
        double p = 0.25;
        int V1 = V/2;
        int V2 = V - V1;

        for(int i = 0; i < V; i++){
            verts.add(i);
            horizontal.put(i,true);
            vertical.put(i,true);
            speed.put(i, (int) (Math.floor(Math.random()*4)+2));
        }
        for(int i = 0; i < verts.size(); i++){
            vertCoords.put(i,createCoords());
        }
        

        //new Simple Graph named printPath
        Graph printPath = simple(V, E);
        PrintStream terminal = System.out;
        try{
            PrintStream graphtxt = new PrintStream(new File("Graph.txt"));
            System.setOut(graphtxt);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        StdOut.println(printPath);
        System.setOut(terminal);

        File graph = new File("Graph.txt");
        try {
            Scanner sc = new Scanner(graph);
            sc.nextLine();
            while (sc.hasNext()) {
                String i = sc.next();
                fileRead.add(i);
            }
            sc.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int linkVert1 = 0;
        int linkVert2 = 0;
        for(int i = 0; i < fileRead.size(); i++){
            if(fileRead.get(i).endsWith(":")){
                String num = fileRead.get(i);
                num = num.substring(0,num.length()-1);
                linkVert1 = Integer.valueOf(num);
            }
            else{
                String num = fileRead.get(i);
                linkVert2 = Integer.valueOf(num);
                if(!weights.containsKey(String.valueOf(linkVert1)+String.valueOf(linkVert2))){
                    edges.add(createEdge(linkVert1,linkVert2));
                }
            }
        }
    }

    //makes text file for graph and weights
    public void gWeights(){
        PrintStream terminal = System.out;
        try{
            PrintStream graphWtxt = new PrintStream(new File("GraphW.txt"));
            System.setOut(graphWtxt);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println(edges.size());
        for(int i = 0; i < edges.size(); i++){
            int edgev1 = edges.get(i).get(0);
            int edgev2 = edges.get(i).get(1);
            System.out.print(edgev1+" "+edgev2+" "+weights.get(String.valueOf(edgev1)+String.valueOf(edgev2)));
            System.out.println();
        }
        System.setOut(terminal);
    }

    //makes text file with whole MWST
    public void gMWST(){
        PrintStream terminal = System.out;
        try{
            PrintStream graphMWSTtxt = new PrintStream(new File("GraphMWST.txt"));
            System.setOut(graphMWSTtxt);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        DSGraph G = new DSGraph();
        G.readGraph("GraphW.txt");
        System.out.println(G.MWST());
        System.setOut(terminal);
    }

    //read gMWST makes arrayList "key" of correct edges
    public void readMWSTEdges(){
        fileRead.clear();
        File graph = new File("GraphMWST.txt");
        try {
            Scanner sc = new Scanner(graph);
            sc.nextLine();
            while (sc.hasNext()) {
                String i = sc.next();
                //System.out.println(i);
                fileRead.add(i);
            }
            sc.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int linkVert1 = 0;
        int linkVert2 = 0;
        for(int i = 0; i < fileRead.size(); i++){
            if(fileRead.get(i).endsWith(":")){
                String num = fileRead.get(i);
                num = num.substring(0,num.length()-1);
                linkVert1 = Integer.valueOf(num);
            }
            else{
                String num = fileRead.get(i);
                linkVert2 = Integer.valueOf(num);
                if(!treeCheck.containsKey(String.valueOf(linkVert1)+String.valueOf(linkVert2))){
                    MWSTEdges.add(createMWSTEdge(linkVert1,linkVert2));
                    treeCheck.put(String.valueOf(linkVert1)+String.valueOf(linkVert2), true);
                    treeCheck.put(String.valueOf(linkVert2)+String.valueOf(linkVert1), true);
                }
            }
        }
    }

    // This clears the ArrayLists and Hashmaps when the back button is pressed so that a new level can be picked and new arraylists and hashmaps can be placed
    public static void clear(){
        verts = new ArrayList<Integer>();
        vertCoords = new HashMap<Integer, ArrayList<Integer>>();
        edges = new ArrayList<ArrayList<Integer>>();
        weights = new HashMap<String, Integer>();
        fileRead = new ArrayList<String>();
        vertical = new HashMap<Integer, Boolean>();
        horizontal = new HashMap<Integer, Boolean>();
        speed = new HashMap<Integer, Integer>();
        MWSTEdges = new ArrayList<ArrayList<Integer>>();
        treeCheck = new HashMap<String, Boolean>();
    }
    

    //ALL OF THE JBUTTONS AND JPANELS
    JFrame MWST;
    
    //main menu
    JPanel ChooseLevel;
    JButton B1;
    JButton B2;
    JButton B3;
    
    //Start screen
    JPanel S;
    JButton S1;
    JButton Back;
    
    //game screen
    int Level;
    JPanel Game;
    JLabel Timer;
    JPanel Clock;
    JPanel Graph;
    JButton Back2;
    
    //leaderboard
    JPanel LeadBoard;
    JLabel Top;
    JLabel Name;
    JLabel Time;
    JLabel first;
    JLabel second;
    JLabel third;
    JLabel fourth;
    JLabel fifth;
    
    //constants
    int Width = 875;
    int Height = 700;
    
    // timer
    long startTime;
    
    public FinalProject(){
        MWST = new JFrame("Find the MWST");
        
        
        //main menu screen
        ChooseLevel = new JPanel();
        ChooseLevel.setPreferredSize(new Dimension(800, 600));
        ChooseLevel.setLayout(new GridLayout(9, 9));
        //buttons for main menu screen
        B1 = new JButton("Level 1");
        B1.setPreferredSize(new Dimension (50, 40));
        B2 = new JButton("Level 2");
        B2.setPreferredSize(new Dimension (50, 40));
        B3 = new JButton("Level 3");
        B3.setPreferredSize(new Dimension (50, 40));
        
        //2nd menu screen
        //top of start screen/graph screen
        S = new JPanel();
        S1 = new JButton("START");
        Back = new JButton("Back");
        
        //game screen
        Game = new JPanel();
        Clock = new JPanel();
        Timer = new JLabel();
        Back2 = new JButton("Back");
        
        //Creates the circles and drawlslines between the circles according to the trees  provided by the arraylist
        Graph = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                for(int i = 0; i < verts.size(); i++){
                    g2.setColor(Color.BLUE);
                    g2.drawOval(vertCoords.get(i).get(0),vertCoords.get(i).get(1),25,25);
                }
                for(int i = 0; i < edges.size(); i++){
                    g2.setColor(Color.BLACK);
                    setFont(new Font("TimesRoman", Font.BOLD, 14));
                    g2.drawString(Integer.toString(weights.get(String.valueOf(edges.get(i).get(0))+
                        String.valueOf(edges.get(i).get(1)))),
                    (vertCoords.get(edges.get(i).get(0)).get(0)+10+vertCoords.get(edges.get(i).get(1)).get(0)+10)/2,
                    (vertCoords.get(edges.get(i).get(0)).get(1)+15+vertCoords.get(edges.get(i).get(1)).get(1)+15)/2);
                    g2.setColor(Color.BLUE);
                    g2.setStroke(new BasicStroke(1));
                    g2.drawLine(vertCoords.get(edges.get(i).get(0)).get(0)+10,vertCoords.get(edges.get(i).get(0)).get(1)+15,
                     vertCoords.get(edges.get(i).get(1)).get(0)+10,vertCoords.get(edges.get(i).get(1)).get(1)+15);
                }
                for(int i = 0; i < MWSTEdges.size(); i++){
                    g2.setColor(Color.RED);
                    g2.drawLine(vertCoords.get(MWSTEdges.get(i).get(0)).get(0)+10,vertCoords.get(MWSTEdges.get(i).get(0)).get(1)+15,
                     vertCoords.get(MWSTEdges.get(i).get(1)).get(0)+10,vertCoords.get(MWSTEdges.get(i).get(1)).get(1)+15);
                }
                repaint();
            }
        };
        
        
        
        //end of game screen
        LeadBoard = new JPanel();
        Top = new JLabel("Top Five Times");
        Name = new JLabel("Name");
        Time = new JLabel("Time");
        first = new JLabel("1");
        second = new JLabel("2");
        third = new JLabel("3");
        fourth = new JLabel("4");
        fifth = new JLabel("5");
        
        MWST.setSize(Width, Height);
        //click x to close the window
        MWST.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //add panels to level screen and start screen
        MWST.add(ChooseLevel);
        
        //set menu colors
        Color ChooseLevelBGC = new Color(213, 173, 245);
        ChooseLevel.setBackground(ChooseLevelBGC);
        //adds buttons to main menu panel
        ChooseLevel.add(B1);
        ChooseLevel.add(B2);
        ChooseLevel.add(B3);
        B1.addActionListener(this);
        B2.addActionListener(this);
        B3.addActionListener(this);
        
        //set start screen colors
        Color StartBGC = new Color(144, 255, 177);
        S.setBackground(StartBGC);
        //add buttons to start screen
        S.add(S1);
        S.add(Back);
        S1.addActionListener(this);
        Back.addActionListener(this);
        
        //set game background color
        Color GameBGC = new Color(188, 251,255);
        Game.setBackground(GameBGC);
        //add buttons and extras to game
        Clock.setBackground(ChooseLevelBGC);
        Game.add(Clock);
        Clock.add(Timer);
        Color GraphBGC = new Color(255, 249, 151);
        Graph.setBackground(GraphBGC);
        Game.add(Back2);
        Back2.addActionListener(this);
        
        MWST.setVisible(true);
    }
    
    
    //Updates the JFrame with the new coordinates of the circles, passes this on to the function earlier that draws newlines between the points and places it on screen
    public void update() {
        for(int i = 0; i < verts.size(); i++){
            int prevX = vertCoords.get(i).get(0);
            int prevY = vertCoords.get(i).get(1);
            vertCoords.put(i,moveCoords(i,prevX,prevY));
        }
    }
    
    // In game stopwatch timer, measures in seconds
    // https://stackoverflow.com/questions/44453825/how-do-i-correctly-restart-a-java-util-timer
    private void launchSomeTimer() {
        TimerTask timerTask = new TimerTask() {
            
            @Override
            public void run() {
                long now = System.currentTimeMillis();
                long gameTime = now - startTime;
                Timer.setText("" + (TimeUnit.MILLISECONDS.toMinutes(gameTime))+ "min " + singleSec(TimeUnit.MILLISECONDS.toSeconds(gameTime)) + (TimeUnit.MILLISECONDS.toSeconds(gameTime)%60) + "sec");
                update();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 100, 100);
        
    }
    
    public static String singleMilli(long i){
        if( i%100 < 10){
            return "0";
        }
        else return "";
    }
    
    // Test
    public static String singleSec(long i){
        if( i%60 < 10){
            return "0";
        }
        else return "";
    }

    // Watches for a mouse click to decide which level to prepare for the user.
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == B1){
            ChooseLevel.setVisible(false);
            MWST.add(S);
            S.setVisible(true);
            Level = 1;
                //goes to level one graph generator with start button
                //graph.setVisible(false) until start button is clicked
            generate(4);
        }
        if(source == B2){
            ChooseLevel.setVisible(false);
            MWST.add(S);
            S.setVisible(true);
            Level = 2;
            generate(5);
        }
        if(source == B3){
            ChooseLevel.setVisible(false);
            MWST.add(S);
            S.setVisible(true);
            Level = 3;
            generate(6);
        }
        if(source == S1){
            S.setVisible(false);
            MWST.add(Game);
            Game.setVisible(true);
            startTime = System.currentTimeMillis();
            launchSomeTimer();
            Graph.setPreferredSize(new Dimension(800, 600));
            Game.add(Graph);
            gWeights();
            gMWST();
            readMWSTEdges();
        }
        //These back buttons direct the user to the main menu and clear the hasmaps and array lists using the clear() function created above
        if(source == Back){
            S.setVisible(false);
            ChooseLevel.setVisible(true);
            clear();
        }
        if(source == Back2){
            Game.setVisible(false);
            ChooseLevel.setVisible(true);
            clear();
        }
    }


    // Generates the graph
    //from https://algs4.cs.princeton.edu/41graph/GraphGenerator.java.html
    public class GraphGenerator {
        private final class Edge implements Comparable<Edge> {
            private int v;
            private int w;
            
            private Edge(int v, int w) {
                if (v < w) {
                    this.v = v;
                    this.w = w;
                }
                else {
                    this.v = w;
                    this.w = v;
                }
            }
            
            public int compareTo(Edge that) {
                if (this.v < that.v) return -1;
                if (this.v > that.v) return +1;
                if (this.w < that.w) return -1;
                if (this.w > that.w) return +1;
                return 0;
            }
        }
    }
    
    /*
     * Returns an Eulerian path graph on {@code V} vertices.
     *
     * @param  V the number of vertices in the path
     * @param  E the number of edges in the path
     * @return a graph that is an Eulerian path on {@code V} vertices
     *         and {@code E} edges
     * @throws IllegalArgumentException if either {@code V <= 0} or {@code E < 0}
     */
    public static Graph eulerianPath(int V, int E) {
        if (E < 0)
            throw new IllegalArgumentException("negative number of edges");
        if (V <= 0)
            throw new IllegalArgumentException("An Eulerian path must have at least one vertex");
        Graph G = new Graph(V);
        int[] vertices = new int[E+1];
        for (int i = 0; i < E+1; i++)
            vertices[i] = StdRandom.uniform(V);
        for (int i = 0; i < E; i++) {
            G.addEdge(vertices[i], vertices[i+1]);
        }
        return G;
    }
    
    /**
     * Returns a random simple graph on {@code V} vertices, with an
     * edge between any two vertices with probability {@code p}. This is sometimes
     * referred to as the Erdos-Renyi random graph model.
     * @param V the number of vertices
     * @param p the probability of choosing an edge
     * @return a random simple graph on {@code V} vertices, with an edge between
     *     any two vertices with probability {@code p}
     * @throws IllegalArgumentException if probability is not between 0 and 1
     */
    public static Graph simple(int V, double p) {
        if (p < 0.0 || p > 1.0)
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        Graph G = new Graph(V);
        for (int v = 0; v < V; v++)
            for (int w = v+1; w < V; w++)
                if (StdRandom.bernoulli(p))
                    G.addEdge(v, w);
                return G;
            }
            
    /**
     * Returns a random simple graph containing {@code V} vertices and {@code E} edges.
     * @param V the number of vertices
     * @param E the number of vertices
     * @return a random simple graph on {@code V} vertices, containing a total
     *     of {@code E} edges
     * @throws IllegalArgumentException if no such simple graph exists
     */
    public static Graph simple(int V, int E) {
        if (E > (long) V*(V-1)/2) throw new IllegalArgumentException("Too many edges");
        if (E < 0)                throw new IllegalArgumentException("Too few edges");
        Graph G = new Graph(V);
        SET<String> set = new SET<String>();
        while (G.E() < E) {
            int v = StdRandom.uniform(V);
            int w = StdRandom.uniform(V);
            String e1 = String.valueOf(v)+String.valueOf(w);
            String e2 = String.valueOf(w)+String.valueOf(v);
            if ((v != w) && !set.contains(e1)) {
                set.add(e1);
                set.add(e2);
                G.addEdge(v, w);
            }
        }
        return G;
    }
    
    public static void main(String[] args){
        FinalProject finals = new FinalProject();
    }
}