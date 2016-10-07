public class Node{
	String node;
	int weight;
	int pathcost;
	int admiscost;
	
	Node(String node, int weight){
		this.node = node;
		this.weight = weight ;
		this.pathcost = 0;
		this.admiscost = 0;
	}
	Node(String node){
		this.node = node;
		this.weight = 0 ;
		this.pathcost = 0;
		this.admiscost = 0;
	}	
	Node(){
		this.node = "";
		this.weight = 0 ;
		this.pathcost = 0;
		this.admiscost = 0;
	}	
	public String getNode(){
		return this.node;
	}
	public int getWeight(){
		return this.weight;
	}
	public int getPathcost(){
		return this.pathcost;
	}
	public int getAdmiscost(){
		return this.admiscost;
	}
	public void setNode(String node){
		this.node = node;
	}
	public void setWeight(int weight){
		this.weight = weight;
	}
	public void setPathcost(int val){
		this.pathcost = val;
	}
	public void setAdmiscost(int val){
		this.admiscost= val;
	}
}
