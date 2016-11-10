package Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
	private String id; 
	private Set<Vertex> nodes;
	private Set<Edge> edges;
	private Map<String, Vertex>	nodeMap;
	private Map<String, Edge> edgeMap;
	private Map<Vertex, Set<Vertex>> adjMatrix;
	
	public Graph(String id) {
		this.id = id;
		this.nodes = new HashSet<Vertex>();
		this.edges = new HashSet<Edge>();
		this.nodeMap = new HashMap<String, Vertex>();
		this.edgeMap = new HashMap<String, Edge>();
		this.adjMatrix = new HashMap<Vertex, Set<Vertex>>(); 
	}

	public Set<Vertex> getNodeSet() {
		return nodes;
	}
	public Set<Edge> getEdgeSet() {
		return edges;
	}
	public Map<String, Vertex> getNodeMap() {
		return nodeMap;
	}
	public Map<String, Edge> getEdgeMap() {
		return edgeMap;
	}
	public Map<Vertex, Set<Vertex>> getAdjMatrix() {
		return adjMatrix;
	}
	
	public Vertex getNode(String id) {
		return nodeMap.get(id);
	}
	
	public boolean containNode(String id) {
		return nodeMap.containsKey(id);
	}
	
	public Edge getEdge(String id) {
		return edgeMap.get(id);
	}
	
	public boolean containEdge(String id) {
		return edgeMap.containsKey(id);
	}
	
	public Edge getEdge(Vertex source, Vertex target) {
		return edgeMap.get(source.getId()+'-'+target.getId());
	}
	
	public void addNode(Vertex node) {
		if (nodeMap.containsKey(node.getId()))
			return;
		nodes.add(node);
		nodeMap.put(node.getId(), node);
	}
	
	public Vertex addNode(String nodeId) {
		if (nodeMap.containsKey(nodeId)) {
			return getNode(nodeId);
		}
		Vertex node = new Vertex(nodeId);
		addNode(node);
		return node;
	}
	
	public void addEdge(Edge edge) {
		if (edgeMap.containsKey(edge.getId()))
			return;
		edges.add(edge);
		edgeMap.put(edge.getId(), edge);
		if (!adjMatrix.containsKey(edge.getSender()))
			adjMatrix.put(edge.getSender(), new HashSet<Vertex>());
		adjMatrix.get(edge.getSender()).add(edge.getReceiver());
	}
	
	public Edge addEdge(String edgeId, String senderId, String receiverId) {
		if (edgeMap.containsKey(edgeId)) 
			return getEdge(edgeId);
		Edge edge = new Edge(edgeId, getNode(senderId), getNode(receiverId));
		addEdge(edge);
		return edge;		
	}
	
	public String getGraphId() {
		return id;
	}
	
	public void setGraphId(String graphId) {
		this.id = graphId;
	}
	
	public int getNodeCount() {
		return nodes.size();
	}
	
	public int getEdgeCount() {
		return edges.size();
	}
	
}
	
	
	
	