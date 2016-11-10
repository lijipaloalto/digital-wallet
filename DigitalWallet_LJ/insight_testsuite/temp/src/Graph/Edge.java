package Graph;

public class Edge {
	private final String id;
	private final Vertex sender;
	private final Vertex receiver;

	public Edge(String id, Vertex sender, Vertex receiver) {
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
	}

	public String getId() {
		return id;
	}

	public Vertex getSender() {
		return sender;
	}

	public Vertex getReceiver() {
		return receiver;
	}

	public String toString() {
		return sender + "-" + receiver;
	}

}
