package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Graph.Graph;
import Graph.Vertex;

public class BFSearch {
	private Vertex source; 
	private Set<Vertex> settledNodes;
	private List<Vertex> unSettledNodes;   //''queue''
	private Map<Vertex, Integer> distance;
	private Map<Vertex, Vertex> predecessors;
	
	public void execute(Graph graph, Vertex start, int step) {
		this.source = start;
		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new LinkedList<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (!unSettledNodes.isEmpty()) {
			Vertex current = unSettledNodes.remove(0);
			settledNodes.add(current);
			
			if (getShortestDistance(current) == step) continue;
			
			Set<Vertex> neighbors = getNeighbors(current, graph);
			for (Vertex target : neighbors) {
				if (isSettled(target) || isUnsettled(target)) continue;
				distance.put(target, getShortestDistance(current)+1);
				predecessors.put(target, current);
				unSettledNodes.add(target);
			}
		}
		
	}
	
	private boolean isSettled(Vertex node) {
		return settledNodes.contains(node);
	}
	
	private boolean isUnsettled(Vertex node) {
		return unSettledNodes.contains(node);
	}
	
	private Set<Vertex> getNeighbors(Vertex node, Graph graph) {
		return graph.getAdjMatrix().get(node);
	}
	
	private int getShortestDistance(Vertex node) {
		Integer d = distance.get(node);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}
	
	public double getPathDistance(Vertex node) {
		return distance.get(node);
	}
	
	public Set<Vertex> getSettledNodes() {
		return settledNodes; 
	}
	
    /*
    * This method returns the path from the source to the selected target and
    * NULL if no path exists
    */
	public LinkedList<Vertex> getPath(Vertex target) {
	    LinkedList<Vertex> path = new LinkedList<Vertex>();
	    Vertex step = target;
	    // check if a path exists
	    if (predecessors.get(step) == null) {
	      return null;
	    }
	    path.add(step);
	    while (predecessors.get(step) != null) {
	      step = predecessors.get(step);
	      path.add(step);
	    }
	    // Put it into the correct order
	    Collections.reverse(path);
	    return path;
	}
		
}
