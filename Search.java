import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;


public class Search {
	public static int first = 0 ;
	public static void display(Stack<Node> st, int flag){
		try {
			File file = new File("output.txt");			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			while(!st.isEmpty()){
				Node x = st.pop();
				if(flag >= 0)
					writer.write(x.getNode() + " " + (flag++));
				else
					writer.write(x.getNode() + " " + x.getPathcost());
			
				writer.newLine();
			}
			writer.close();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
		
	public static Node contains(LinkedList<Node> list, Node x){
		Iterator<Node> it = list.iterator();
		while(it.hasNext()){
			Node k = it.next();
			if(x.getNode().equals(k.getNode())){
				return k;
			}
		}
		return x;
	}
	
	public static boolean present(LinkedList<Node> list, Node x){
		Iterator<Node> it = list.iterator();
		while(it.hasNext()){
			Node k = it.next();
			if(x.getNode().equals(k.getNode())){
				return true;
			}
		}
		return false;
	}
	
	public static int dfsSearch(String startState, String goalState, HashMap<String, ArrayList<Node>> hmap){
		Stack<Node> display = new Stack<Node>();	
		display.clear();
		Node start = new Node(startState,0);
		Stack<Node> dfsStack = new Stack<Node>();
		dfsStack.clear();
		Stack<Node> temp = new Stack<Node>();	
		Node currnode;
		HashMap <String, Integer>explored = new HashMap<String, Integer>();
		HashMap <String, Node>parent = new HashMap<String, Node>();
		
		dfsStack.add(start);
		parent.put(start.getNode(), null);
		explored.put(start.getNode(),1);
		System.out.print("DFS: ");
		while(dfsStack.size()>0){			
			currnode = dfsStack.pop();						
			if(!hmap.containsKey(currnode.getNode()))
				continue;
			Iterator <Node> itr = hmap.get(currnode.getNode()).iterator();
			while(itr.hasNext()){
				Node child = itr.next();
				if(!explored.containsKey(child.getNode()))
				{
					explored.put(child.getNode(),1);
					parent.put(child.getNode(), currnode);
					temp.push(child);
					if(child.getNode().equals(goalState)){
						while(child != null){
							display.push(child);
							child = parent.get(child.getNode());
						}
						display(display,0);
						return 0;
					}
				}		
			}
			while(!temp.empty())
				dfsStack.push(temp.pop());
		}
		return 0;
	}
	
	public static int bfsSearch(String startState, String goalState, HashMap<String, ArrayList<Node>> hmap){
		Stack<Node> display = new Stack<Node>();	
		display.clear();
		Node start = new Node(startState,0);
		LinkedList<Node> bfsqueue = new LinkedList<Node>();	
		bfsqueue.clear();
		Node currnode;
		HashMap <String, Integer>explored = new HashMap<String, Integer>();
		HashMap <String, Node>parent = new HashMap<String, Node>();
		
		bfsqueue.add(start);
		parent.put(start.getNode(), null);
		explored.put(start.getNode(),1);
		System.out.print("BFS: ");
		while(bfsqueue.size()>0){			
			currnode = bfsqueue.poll();	
			if(!hmap.containsKey(currnode.getNode()))
				continue;
			Iterator <Node> itr = hmap.get(currnode.getNode()).iterator();
			while(itr.hasNext()){
				Node child = itr.next();
				if(!explored.containsKey(child.getNode()))
				{
					explored.put(child.getNode(),1);
					parent.put(child.getNode(), currnode);
					bfsqueue.add(child);
					if(child.getNode().equals(goalState)){
						while(child != null){
							display.push(child);
							child = parent.get(child.getNode());
						}
						display(display,0);
						return 0;
					}
				}
			}
		}
		return 0;
	}
	
	public static int ucsSearch(String startState, String goalState, HashMap<String, ArrayList<Node>> hmap){
		Comparator<Node> compare=new Comparator<Node>() {
	        @Override
	        public int compare(Node n1, Node n2) {
	            if (n1.getPathcost() > n2.getPathcost()) return 1;
	            if (n1.getPathcost() < n2.getPathcost()) return -1;
	            return 0;
	        }
	    };
		LinkedList<Node> open = new LinkedList<Node>();
		LinkedList<Node> closed = new LinkedList<Node>();
		Stack<Node> st = new Stack<Node>();
		open.clear();
		closed.clear();
		st.clear();
		HashMap <String, Node>parent = new HashMap<String, Node>();
		Node start = new Node(startState,0);
		open.add(start);
		parent.put(startState,null);
		while(!open.isEmpty()){
			Node currnode = open.poll();
			if(currnode.getNode().equals(goalState)){
				while(currnode != null){
					st.push(currnode);
					currnode = parent.get(currnode.getNode());
				}
				System.out.print("UCS : ");
				display(st, -1);
				return 0;
			}
			
			if(!hmap.containsKey(currnode.getNode()) || hmap.get(currnode.getNode()).isEmpty())
				continue;
			Iterator<Node> itr = hmap.get(currnode.getNode()).iterator();
			while(itr.hasNext()){
				Node child = itr.next();
				child.setPathcost(currnode.getPathcost() + child.getWeight());
				Node node = new Node(child.getNode());
				if(contains(open,child).equals(child) && contains(closed,child).equals(child)){
					open.add(child);
					parent.put(child.getNode(),currnode);
				}
				else if(!(node=contains(open,child)).equals(child)){
					if(child.getPathcost()<node.getPathcost()){
						open.remove(node);
						open.add(child);
						parent.put(child.getNode(),currnode);
						
					}
				}
				else if(!(node=contains(closed,child)).equals(child)){
					if(child.getPathcost()<node.getPathcost()){
						closed.remove(node);
						open.add(child);
						parent.put(child.getNode(),currnode);
					}
				}
			}
			closed.add(currnode);
			open.sort(compare);
					}
		return 0;
	}
	
	public static int astarSearch(String startState, String goalState, HashMap<String, ArrayList<Node>> hmap, HashMap<String,Integer> huermap){
		Comparator<Node> comp=new Comparator<Node>() {
	        @Override
	        public int compare(Node n1, Node n2) {
	            if (n1.getAdmiscost() > n2.getAdmiscost()) return 1;
	            if (n1.getAdmiscost() < n2.getAdmiscost()) return -1;
	            return 0;
	        }
	    };
		LinkedList<Node> open = new LinkedList<Node>();
		LinkedList<Node> closed = new LinkedList<Node>();
		Stack<Node> st = new Stack<Node>();
		open.clear();
		closed.clear();
		st.clear();
		HashMap <String, Node>parent = new HashMap<String, Node>();
		parent.clear();
		
		Node start = new Node(startState,0);
		open.add(start);
		parent.put(startState,null);
		while(!open.isEmpty()){
			Node currnode = open.poll();	
			if(currnode.getNode().equals(goalState)){
				while(currnode != null){
					System.out.println(currnode.getNode() + " " +currnode.getPathcost());
					st.push(currnode);
					Node prev = currnode;
					currnode = parent.get(prev.getNode());
					
				}
				System.out.print("A* : ");
				display(st, -1);
				return 0;
			}
			
			if(!hmap.containsKey(currnode.getNode()) || hmap.get(currnode.getNode()).isEmpty())
				continue;
			Iterator<Node> itr = hmap.get(currnode.getNode()).iterator();
		
			while(itr.hasNext()){
				Node storedNode = itr.next();
				Node child=new Node();
				child.setNode(storedNode.getNode());
				child.setAdmiscost(storedNode.getAdmiscost());
				child.setPathcost(storedNode.getPathcost());
				child.setWeight(storedNode.getWeight());
				
				
				
				if(!huermap.containsKey(child.getNode()))
					return 0;
				child.setPathcost(currnode.getPathcost()+child.getWeight());
				child.setAdmiscost(huermap.get(child.getNode())+ child.getPathcost());
				
				Node node=new Node();		
				if((!present(open,child)) && (!present(closed,child))){
					parent.put(child.getNode(),currnode);
					open.add(child);
					//System.out.print(child.okay + " " + child.getNode() + "-" + child.getPathcost() + " "+parent.get(child.getNode()).getPathcost()+"|");
					
				}
				else if(!(node=contains(open,child)).equals(child)){
					if(child.getPathcost()<node.getPathcost()){
						open.remove(node);
						parent.put(child.getNode(),currnode);
						open.add(child);
						//System.out.print(child.okay + " " + child.getNode() + "-" + child.getPathcost() + " "+parent.get(child.getNode()).getPathcost()+"|");
						
					}
				}
				else if(!(node=contains(closed,child)).equals(child)){
					if(child.getPathcost()<node.getPathcost()){
						closed.remove(node);
						parent.put(child.getNode(),currnode);
						open.add(child);
						//System.out.print(child.okay + " " + child.getNode() + "-" + child.getPathcost() + " "+parent.get(child.getNode()).getPathcost()+"|");
						
					}
				}
			}
			closed.add(currnode);
			open.sort(comp);		
		}
		return 0;
	}
		
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub       
		try{
	
        	File file = new File("input.txt");
            Scanner scanner = new Scanner(file);
        	String searchType = scanner.nextLine().trim();
        	String startState = scanner.nextLine().trim();
        	String goalState = scanner.nextLine().trim();
        	int trafficNum = scanner.nextInt();
        	
        	HashMap <String, ArrayList<Node>> hmap = new HashMap<String, ArrayList<Node>>();
        	HashMap <String, Integer> huermap = new HashMap<String, Integer>();
        	for(int i =0; i<trafficNum ; i++){
        		String source = scanner.next();
        		String destination = scanner.next();
        		int weight = scanner.nextInt();
        		
        		if(hmap.get(source) == null){
        			hmap.put(source, new ArrayList<Node>());
        		}
        		hmap.get(source).add(new Node(destination,weight));
        	}
        	if(scanner.hasNextLine()){
	        	int sundayNum = scanner.nextInt();
	        	for(int i =0; i<sundayNum ; i++){
	        		huermap.put(scanner.next(), scanner.nextInt());
	        	}
        	}
        	scanner.close();
        	
        	if(startState.equals(goalState)){
    			Stack<Node> display = new Stack<Node>();
    			display.push(new Node(startState));
    			display(display,0);
    			System.exit(0);
    		}
        	
        	
        	int retvalue; 	
        	switch(searchType){
	        	case "BFS" : retvalue = bfsSearch(startState, goalState, hmap);
	        				break;
	        	case "DFS" : retvalue = dfsSearch(startState, goalState, hmap);
	        				break;
	        	case "UCS" : retvalue = ucsSearch(startState, goalState, hmap);
	        				break;
	        	case "ASTAR" : retvalue = astarSearch(startState, goalState, hmap, huermap);
	        				break;
	        	case "A*" : retvalue = astarSearch(startState, goalState, hmap, huermap);
	        				break;

        	}
        }
        catch(Exception e) {
       	 e.printStackTrace();
       }
	}
}
