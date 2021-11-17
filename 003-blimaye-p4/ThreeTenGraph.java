import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;

import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.EdgeType;

import org.apache.commons.collections15.Factory;

import java.util.Collection;
import java.util.ArrayList;


/**
 * Class represents a graph or a collection of vertices and edges.
 * @author Katherine (Raven) Russell 
 * @author Brian Limaye
 */
class ThreeTenGraph implements Graph<GraphNode,GraphEdge>, UndirectedGraph<GraphNode,GraphEdge> {
	
	/**
	 * Max number of nodes/vertices.
	 */
	private static final int MAX_NUMBER_OF_NODES = 200;
	
	/**
	 * Adjacency matrix responsible for keeping track of vertices and connected edges.
	 */
	private GraphComp[][] storage = null;

	/**
	 * Helper method to check if all vertices have been visited in a DFT.
	 * @param visited Array representing whether a certain vertex has been visited or not.
	 * @return Returns true if all vertices have been visited, false otherwise.
	 */
	private boolean isCompletelyTrue(boolean[] visited) {

		Collection<GraphNode> vertices = getVertices();

		for(GraphNode curr: vertices) {

			if(!visited[curr.getId()]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Helper method to find the lowest ID vertex that hasn't been visited yet.
	 * @param visited Array representing whether a certain vertex has been visited or not.
	 * @return Returns the lowest ID vertex, if any.
	 */
	private GraphNode findLowestID(boolean[] visited) {

		//Default value for minimum ID.
		int minId = Integer.MAX_VALUE;
		GraphNode min = null;

		for(int i = 0; i < storage.length; i++) {

			//Verification that the vertex exists and has not been visited, prior to comparison.
			if((storage[i][i] != null) && (!visited[i])) {

				int currId = storage[i][i].getId();

				if(currId < minId) {

					minId = currId;
					min = (GraphNode) storage[i][i];
				}
			}
		}

		return min;
	}

	/**
	 * Helper recursive function to perform the DFT in an overloaded fashion to keep track of the path.
	 * @param curr The current GraphNode.
	 * @param visited Array representing whether a certain vertex has been visited or not.
	 * @param restart Flag indicating whether the algorithm must restart at the lowest ID vertex or not.
	 * @param path The current path of the traversal.
	 * @return Returns the final path of the DPT.
	 */
	private String depthFirstTraversal(GraphNode curr, boolean[] visited, boolean restart, StringBuilder path) {
		
		RecursionCheck.hasRecursion(); //DO NOT REMOVE THIS LINE

		//Base Case 1: When all vertices have been visited, indicating completion.
		if(isCompletelyTrue(visited)) {
			return path.toString();
		}

		//Case where a restart must occur.
		if((restart) && (curr == null)) {
			depthFirstTraversal(findLowestID(visited), visited, false, path);
		}

		if(curr == null) {
			return depthFirstTraversal(null, visited, true, path);
		}

		//Updates the current path at the given vertex.
		int id = curr.getId();
		path.append(id + " ");
		visited[id] = true;

		Collection<GraphNode> neighbors = getNeighbors(curr);

		//Moves to the neighbor vertex that has not been visited yet, if possible.
		for(GraphNode currNeigh: neighbors) {

			int currId = currNeigh.getId();

			if(!visited[currId]) {
				depthFirstTraversal(currNeigh, visited, false, path);
			}
		}

		return "";
	}
	
	/**
	 * Default constructor to initialize a new instance of ThreeTenGraph with an adjacency matrix of max size.
	 */
	public ThreeTenGraph() {
		this.storage = new GraphComp[MAX_NUMBER_OF_NODES][MAX_NUMBER_OF_NODES];
	}
	
    /**
     * Returns a view of all edges in this graph. In general, this
     * obeys the Collection contract, and therefore makes no guarantees 
     * about the ordering of the vertices within the set.
     * @return a Collection view of all edges in this graph
     */
    public Collection<GraphEdge> getEdges() {

		Collection<GraphEdge> edges = new ArrayList<>();

		for(int i = 0; i < storage.length; i++) {

			for(int j = 0; j < storage[i].length; j++) {

				//Ensures the edge is real/non-null prior to adding.
				if(storage[i][j] instanceof GraphEdge) {
					edges.add((GraphEdge) storage[i][j]);
				}
			}
		}

		return edges;
	}
    
    /**
     * Returns a view of all vertices in this graph. In general, this
     * obeys the Collection contract, and therefore makes no guarantees 
     * about the ordering of the vertices within the set.
     * @return a Collection view of all vertices in this graph
     */
    public Collection<GraphNode> getVertices() {

		Collection<GraphNode> vertices = new ArrayList<>();

		for(int i = 0; i < storage.length; i++) {

			//Ensures the vertex is real/non-null prior to adding.
			if(storage[i][i] instanceof GraphNode) {
				vertices.add((GraphNode) storage[i][i]);
			}
		}
		
		return vertices;		
	}
    
    /**
     * Returns the number of edges in this graph.
     * @return the number of edges in this graph
     */
    public int getEdgeCount() {
		
		int numEdges = 0;

		for(int i = 0; i < storage.length; i++) {

			for(int j = 0; j < storage[i].length; j++) {
				//Increments the number of edges if the element in the adjacency matrix is only a GraphEdge instance.
				numEdges = (storage[i][j] instanceof GraphEdge) ? numEdges + 1: numEdges;
			}
		}
		
		return numEdges / 2;
	}
    
    /**
     * Returns the number of vertices in this graph.
     * @return the number of vertices in this graph
     */
    public int getVertexCount() {

		int numVertices = 0;

		for(int i = 0; i < storage.length; i++) {
			//Increments the number of vertices if the element in the adjacency matrix is only a GraphNode instance.
			numVertices = (storage[i][i] instanceof GraphNode) ? numVertices + 1: numVertices;
		}

		return numVertices;
	}

    /**
     * Returns true if this graph's vertex collection contains vertex.
     * Equivalent to getVertices().contains(vertex).
     * @param vertex the vertex whose presence is being queried
     * @return true iff this graph contains a vertex vertex
     */
    public boolean containsVertex(GraphNode vertex) {

		if(vertex == null) {
			return false;
		}
		
		int id = vertex.getId();

		//Check to ensure that the corresponding position for the vertex in the adjacency matrix is non-null.
		return storage[id][id] != null;
	}
    
    /**
     * Returns the collection of vertices which are connected to vertex
     * via any edges in this graph.
     * If vertex is connected to itself with a self-loop, then 
     * it will be included in the collection returned.
     * 
     * @param vertex the vertex whose neighbors are to be returned
     * @return  the collection of vertices which are connected to vertex, or null if vertex is not present
     */
    public Collection<GraphNode> getNeighbors(GraphNode vertex) {

		if(vertex == null) {
			return null;
		}

		int id = vertex.getId();
		Collection<GraphNode> neighbors = new ArrayList<>();

		for(int i = 0; i < storage[id].length; i++) {

			//Ensures the neighbor is real/non-null prior to adding.
			if(storage[id][i] instanceof GraphEdge) {
				neighbors.add((GraphNode) storage[i][i]);
			}
		}
		
		return neighbors;
	}
    
    /**
     * Returns the number of vertices that are adjacent to vertex
     * (that is, the number of vertices that are incident to edges in vertex's
     * incident edge set).
     * 
     * <p>Equivalent to getNeighbors(vertex).size().
     * @param vertex the vertex whose neighbor count is to be returned
     * @return the number of neighboring vertices
     */
    public int getNeighborCount(GraphNode vertex) {

		if(vertex == null) {
			return 0;
		}

		return getNeighbors(vertex).size();
	}
    
    /**
     * Returns the collection of edges in this graph which are connected to vertex.
     * 
     * @param vertex the vertex whose incident edges are to be returned
     * @return  the collection of edges which are connected to vertex, or null if vertex is not present
     */
    public Collection<GraphEdge> getIncidentEdges(GraphNode vertex) {

		if(vertex == null) {
			return null;
		}

		int id = vertex.getId();

		//Check for a vertex that has not been added to the adjacency matrix.
		if(storage[id][id] == null) {
			return null;
		}

		Collection<GraphEdge> incEdges = new ArrayList<>();

		for(int i = 0; i < storage[id].length; i++) {

			//Ensures the edge is real/non-null prior to adding it as an incident edge.
			if(storage[id][i] instanceof GraphEdge) {
				incEdges.add((GraphEdge) storage[id][i]);
			}
		}

		return incEdges;
	}
    /**
     * Returns the endpoints of edge as a Pair< GraphNode >.
     * @param edge the edge whose endpoints are to be returned
     * @return the endpoints (incident vertices) of edge
     */
    public Pair<GraphNode> getEndpoints(GraphEdge edge) {

		if(edge == null) {
			return null;
		}

		Pair<GraphNode> endpoints = null;

		for(int i = 0; i < storage.length; i++) {

			for(int j = 0; j < storage[i].length; j++) {

				//Ensures the edge is real/non-null prior to extracting its endpoints.
				if((storage[i][j] instanceof GraphEdge) && (edge.equals(storage[i][j]))) {

					GraphNode pointOne = (GraphNode) storage[j][j];
					GraphNode pointTwo = (GraphNode) storage[i][i];
					endpoints = new Pair<>(pointOne, pointTwo);
					break;
				}
			}
		}
		
		return endpoints;
	}

    /**
     * Returns an edge that connects v1 to v2.
     * If this edge is not uniquely
     * defined (that is, if the graph contains more than one edge connecting 
     * v1 to v2), any of these edges 
     * may be returned.  findEdgeSet(v1, v2) may be 
     * used to return all such edges.
     * Returns null if either of the following is true:
     * <ul>
     * <li/>v1 is not connected to v2
     * <li/>either v1 or v2 are not present in this graph
     * </ul> 
     * 
     * <p><b>Note</b>: for purposes of this method, v1 is only considered to be connected to
     * v2 via a given <i>directed</i> edge e if
     * v1 == e.getSource() && v2 == e.getDest() evaluates to true.
     * (v1 and v2 are connected by an undirected edge u if 
     * u is incident to both v1 and v2.)
     * @param v1 The first vertex.
     * @param v2 The second vertex.
     * @return  an edge that connects v1 to v2, or null if no such edge exists (or either vertex is not present)
     * @see Hypergraph#findEdgeSet(Object, Object) 
     */
    public GraphEdge findEdge(GraphNode v1, GraphNode v2) {

		if((v1 == null) || (v2 == null)) {
			return null;
		}

		int idOne = v1.getId();
		int idTwo = v2.getId();

		//Checks if v1 and/or v2 have not yet been added to the adjacency matrix.
		if((storage[idOne][idOne] == null) || (storage[idTwo][idTwo] == null)) {
			return null;
		}

		//Checks to see if there are not any incident edges that connects the two vertices.
		if((storage[idOne][idTwo] == null) && (storage[idTwo][idOne] == null)) {
			return null;
		}

		return (storage[idOne][idTwo] == null) ? (GraphEdge) storage[idTwo][idOne] : (GraphEdge) storage[idOne][idTwo];		
	}

    /**
     * Returns true if vertex and edge 
     * are incident to each other.
     * Equivalent to getIncidentEdges(vertex).contains(edge) and to
     * getIncidentVertices(edge).contains(vertex).
     * @param vertex The vertex to be used.
     * @param edge  The edge used to be determined whether or not it's an incident edge to vertex.
     * @return true if vertex and edge are incident to each other
     */
    public boolean isIncident(GraphNode vertex, GraphEdge edge) {

		if((vertex == null) || (edge == null)) {
			return false;
		}

		return getIncidentEdges(vertex).contains(edge);		
	}

    /**
     * Adds edge e to this graph such that it connects 
     * vertex v1 to v2.
     * Equivalent to addEdge(e, new Pair< GraphNode >(v1, v2)).
     * If this graph does not contain v1, v2, 
     * or both, implementations may choose to either silently add 
     * the vertices to the graph or throw an IllegalArgumentException.
     * If this graph assigns edge types to its edges, the edge type of
     * e will be the default for this graph.
     * See Hypergraph.addEdge() for a listing of possible reasons
     * for failure.
     * @param e the edge to be added
     * @param v1 the first vertex to be connected
     * @param v2 the second vertex to be connected
     * @return true if the add is successful, false otherwise
     * @see Hypergraph#addEdge(Object, Collection)
     * @see #addEdge(Object, Object, Object, EdgeType)
     */
    public boolean addEdge(GraphEdge e, GraphNode v1, GraphNode v2) {

		if((e == null) || (v1 == null) || (v2 == null)) {
			throw new IllegalArgumentException();
		}

		int idOne = v1.getId();
		int idTwo = v2.getId();

		//Check to see if v1 and/or v2 is null in the adjacency matrix.
		if((storage[idOne][idOne] == null) || (storage[idTwo][idTwo] == null)) {
			throw new IllegalArgumentException("Vertices not found...");
		}

		for(int i = 0; i < storage.length; i++) {

			for(int j = 0; j < storage[i].length; j++) {

				//Check to see if e has already been added to the adjacency matrix.
				if((storage[i][j] instanceof GraphEdge) && (storage[i][j].equals(e))) {
					throw new IllegalArgumentException();
				}
			}
		}

		//Sets the edge, connecting the vertices v1 and v2.
		storage[idOne][idTwo] = e;
		storage[idTwo][idOne] = e;
		
		return true;
	}
    
    /**
     * Adds vertex to this graph.
     * Fails if vertex is null or already in the graph.
     * 
     * @param vertex    the vertex to add
     * @return true if the add is successful, and false otherwise
     * @throws IllegalArgumentException if vertex is null
     */
    public boolean addVertex(GraphNode vertex) {
		
		if(vertex == null) {
			throw new IllegalArgumentException();
		}
		
		int vid = vertex.getId();

		//Check to see if vertex has already been added previously.
		if(storage[vid][vid] != null) {
			return false;
		}

		//Sets the vertex into the adjacency matrix.
		storage[vid][vid] = vertex;
		return true;
	}

    /**
     * Removes edge from this graph.
     * Fails if edge is null, or is otherwise not an element of this graph.
     * 
     * @param edge the edge to remove
     * @return true if the removal is successful, false otherwise
     */
    public boolean removeEdge(GraphEdge edge) {

		if(edge == null) {
			return false;
		}

		for(int i = 0; i < storage.length; i++) {

			for(int j = 0; j < storage[i].length; j++) {

				if((storage[i][j] instanceof GraphEdge) && (edge.equals(storage[i][j]))) {

					//Removes the incident edges stored in the adjacency matrix when edge is found.
					storage[i][j] = null;
					storage[j][i] = null;
					return true;
				}
			}
		}
		
		return false;
	}
    
    /**
     * Removes vertex from this graph.
     * As a side effect, removes any edges e incident to vertex if the 
     * removal of vertex would cause e to be incident to an illegal
     * number of vertices.  (Thus, for example, incident hyperedges are not removed, but 
     * incident edges--which must be connected to a vertex at both endpoints--are removed.) 
     * 
     * <p>Fails under the following circumstances:
     * <ul>
     * <li/>vertex is not an element of this graph
     * <li/>vertex is null
     * </ul>
     * 
     * @param vertex the vertex to remove
     * @return true if the removal is successful, false otherwise
     */
    public boolean removeVertex(GraphNode vertex) {

		if((vertex == null) || (!containsVertex(vertex))){
			return false;
		}

		boolean found = false;
		int index = -1;

		for(int i = 0; i < storage.length; i++) {

			//Check if the vertex has been found in the adjacency matrix.
			if((storage[i][i] instanceof GraphNode) && (storage[i][i].equals(vertex))) {

				found = true;
				index = i;
				break;
			}
		}

		//Case where the vertex was not found in the graph.
		if(!found) {
			return false;
		}

		Collection<GraphEdge> incidentEdges = getIncidentEdges(vertex);

		//Removes all associated incident edges with the vertex.
		for(GraphEdge incEdge: incidentEdges) {
			removeEdge(incEdge);
		}

		//Removes the vertex from the adjacency matrix.
		storage[index][index] = null;
		return true;
	}
	
	/**
	 *  Returns a string of the depth first traversal. If curr is not null,
	 *  the traversal will start at the given vertex. If curr is null, it will pick
	 *  the lowest ID vertex to start. If restart is set to false, the traversal
	 *  will only visit vertices reachable from the chosen starting location. If
	 *  restart is set to true, the depth first traversal will visit the lowest
	 *  ID vertex not yet visited if it runs out of vertices reachable from the starting
	 *  location (until all vertices in the graph have been visited).
	 *  
	 *  @param curr the current vertex being worked on, may be null
	 *  @param visited the list of vertices which have already been visited by the algorithm
	 *  @param restart whether or not to restart if all vertices have not been reached
	 *  @return a string representation of the depth first traversal, or an empty string if the graph is empty
	 */
	public String depthFirstTraversal(GraphNode curr, boolean[] visited, boolean restart) {
	
		RecursionCheck.hasRecursion(); //DO NOT REMOVE THIS LINE

		return depthFirstTraversal(curr, visited, restart, new StringBuilder());
	}
	
	//********************************************************************************
	//   testing code goes here... edit this as much as you want!
	//********************************************************************************
	
	/**
	 * Main method primarily used for basic testing of the implementation of ThreeTenGraph.java.
	 * @param args Command-line arguments used to dynamically test the functionality at run-time.
	 */
	public static void main(String[] args) {
		
		GraphNode[] nodes = {
			new GraphNode(0), 
			new GraphNode(1), 
			new GraphNode(2), 
			new GraphNode(3), 
			new GraphNode(4), 
			new GraphNode(5), 
			new GraphNode(6), 
			new GraphNode(7), 
			new GraphNode(8), 
			new GraphNode(9)
		};
		
		GraphEdge[] edges = {
			new GraphEdge(0), 
			new GraphEdge(1), 
			new GraphEdge(2),
			new GraphEdge(3), 
			new GraphEdge(4), 
			new GraphEdge(5)
		};
		
		//constructs a graph
		ThreeTenGraph graph = new ThreeTenGraph();
		for(GraphNode n : nodes) {
			graph.addVertex(n);
		}
		
		
		graph.addEdge(edges[0],nodes[0],nodes[1]);
		graph.addEdge(edges[1],nodes[1],nodes[2]);
		graph.addEdge(edges[2],nodes[3],nodes[2]);
		graph.addEdge(edges[3],nodes[6],nodes[7]);
		graph.addEdge(edges[4],nodes[8],nodes[9]);
		graph.addEdge(edges[5],nodes[9],nodes[0]);
		

		System.out.println(graph.getEdges());
		System.out.println(graph.getVertices());
		
		
		if(graph.getVertexCount() == 10 && graph.getEdgeCount() == 6) {
			System.out.println("Yay 1");
		}
		
		if(graph.containsVertex(new GraphNode(0)) && graph.containsEdge(new GraphEdge(3))) {
			System.out.println("Yay 2");
		}
		
		if(graph.toString().trim().equals("0 1 2 3 9 8 4 5 6 7")) {
			System.out.println("Yay 3");
		}
		
		//lot more testing here...
		
		//If your graph "looks funny" you probably want to check:
		//getVertexCount(), getVertices(), getInEdges(vertex),
		//and getIncidentVertices(incomingEdge) first. These are
		//used by the layout class.
	}
	
    //********************************************************************************
    //   YOU MAY, BUT DON'T NEED TO EDIT THINGS IN THIS SECTION
    //********************************************************************************

    /**
     * Returns true if v1 and v2 share an incident edge.
     * Equivalent to getNeighbors(v1).contains(v2).
     * 
     * @param v1 the first vertex to test
     * @param v2 the second vertex to test
     * @return true if v1 and v2 share an incident edge
     */
    public boolean isNeighbor(GraphNode v1, GraphNode v2) {
		return (findEdge(v1, v2) != null);
	}
    
    /**
     * Returns true if this graph's edge collection contains edge.
     * Equivalent to getEdges().contains(edge).
     * @param edge the edge whose presence is being queried
     * @return true iff this graph contains an edge edge
     */
    public boolean containsEdge(GraphEdge edge) {
		return (getEndpoints(edge) != null);
	}
    
    /**
     * Returns the collection of edges in this graph which are of type edgeType.
     * @param edgeType the type of edges to be returned
     * @return the collection of edges which are of type edgeType, or null if the graph does not accept edges of this type.
     * @see EdgeType
     */
    public Collection<GraphEdge> getEdges(EdgeType edgeType) {
		if(edgeType == EdgeType.UNDIRECTED) {
			return getEdges();
		}
		return null;
	}

    /**
     * Returns the number of edges of type edgeType in this graph.
     * @param edgeType the type of edge for which the count is to be returned
     * @return the number of edges of type edgeType in this graph
     */
    public int getEdgeCount(EdgeType edgeType) {
		if(edgeType == EdgeType.UNDIRECTED) {
			return getEdgeCount();
		}
		return 0;
	}
    
    /**
     * Returns the number of edges incident to vertex.  
     * Special cases of interest:
     * <ul>
     * <li/> Incident self-loops are counted once.
     * <li> If there is only one edge that connects this vertex to
     * each of its neighbors (and vice versa), then the value returned 
     * will also be equal to the number of neighbors that this vertex has
     * (that is, the output of getNeighborCount).
     * <li> If the graph is directed, then the value returned will be 
     * the sum of this vertex's indegree (the number of edges whose 
     * destination is this vertex) and its outdegree (the number
     * of edges whose source is this vertex), minus the number of
     * incident self-loops (to avoid double-counting).
     * </ul>
     * 
     * <p>Equivalent to getIncidentEdges(vertex).size().
     * 
     * @param vertex the vertex whose degree is to be returned
     * @return the degree of this node
     * @see Hypergraph#getNeighborCount(Object)
     */
    public int degree(GraphNode vertex) {
		return getNeighborCount(vertex);
	}
	
    /**
     * Returns a Collection view of the predecessors of vertex 
     * in this graph.  A predecessor of vertex is defined as a vertex v 
     * which is connected to 
     * vertex by an edge e, where e is an outgoing edge of 
     * v and an incoming edge of vertex.
     * @param vertex    the vertex whose predecessors are to be returned
     * @return  a Collection view of the predecessors of vertex in this graph
     */
    public Collection<GraphNode> getPredecessors(GraphNode vertex) {
		return getNeighbors(vertex);
	}
    
    /**
     * Returns a Collection view of the successors of vertex 
     * in this graph.  A successor of vertex is defined as a vertex v 
     * which is connected to 
     * vertex by an edge e, where e is an incoming edge of 
     * v and an outgoing edge of vertex.
     * @param vertex    the vertex whose predecessors are to be returned
     * @return  a Collection view of the successors of vertex in this graph
     */
    public Collection<GraphNode> getSuccessors(GraphNode vertex) {
		return getNeighbors(vertex);
	}
    
    /**
     * Returns true if v1 is a predecessor of v2 in this graph.
     * Equivalent to v1.getPredecessors().contains(v2).
     * @param v1 the first vertex to be queried
     * @param v2 the second vertex to be queried
     * @return true if v1 is a predecessor of v2, and false otherwise.
     */
    public boolean isPredecessor(GraphNode v1, GraphNode v2) {
		return isNeighbor(v1, v2);
	}
    
    /**
     * Returns true if v1 is a successor of v2 in this graph.
     * Equivalent to v1.getSuccessors().contains(v2).
     * @param v1 the first vertex to be queried
     * @param v2 the second vertex to be queried
     * @return true if v1 is a successor of v2, and false otherwise.
     */
    public boolean isSuccessor(GraphNode v1, GraphNode v2) {
		return isNeighbor(v1, v2);
	}
    
    /**
     * If directed_edge is a directed edge in this graph, returns the source; 
     * otherwise returns null. 
     * The source of a directed edge d is defined to be the vertex for which  
     * d is an outgoing edge.
     * directed_edge is guaranteed to be a directed edge if 
     * its EdgeType is DIRECTED. 
     * @param directedEdge The directed edge.
     * @return  the source of directed_edge if it is a directed edge in this graph, or null otherwise
     */
    public GraphNode getSource(GraphEdge directedEdge) {
		return null;
	}

    /**
     * If directed_edge is a directed edge in this graph, returns the destination; 
     * otherwise returns null. 
     * The destination of a directed edge d is defined to be the vertex 
     * incident to d for which  
     * d is an incoming edge.
     * directed_edge is guaranteed to be a directed edge if 
     * its EdgeType is DIRECTED. 
     * @param directedEdge The directed edge.
     * @return  the destination of directed_edge if it is a directed edge in this graph, or null otherwise
     */
    public GraphNode getDest(GraphEdge directedEdge) {
		return null;
	}
	
    /**
     * Returns a Collection view of the incoming edges incident to vertex
     * in this graph.
     * @param vertex    the vertex whose incoming edges are to be returned
     * @return  a Collection view of the incoming edges incident to vertex in this graph
     */
    public Collection<GraphEdge> getInEdges(GraphNode vertex) {
		return getIncidentEdges(vertex);
	}
    
    /**
     * Returns the collection of vertices in this graph which are connected to edge.
     * Note that for some graph types there are guarantees about the size of this collection
     * (i.e., some graphs contain edges that have exactly two endpoints, which may or may 
     * not be distinct).  Implementations for those graph types may provide alternate methods 
     * that provide more convenient access to the vertices.
     * 
     * @param edge the edge whose incident vertices are to be returned
     * @return  the collection of vertices which are connected to edge, or null if edge is not present
     */
    public Collection<GraphNode> getIncidentVertices(GraphEdge edge) {
		
		Pair<GraphNode> p = getEndpoints(edge);
		if(p == null) return null;
		
		ArrayList<GraphNode> ret = new ArrayList<>();
		ret.add(p.getFirst());
		ret.add(p.getSecond());
		return ret;
	}
    
    /**
     * Returns a Collection view of the outgoing edges incident to vertex
     * in this graph.
     * @param vertex    the vertex whose outgoing edges are to be returned
     * @return  a Collection view of the outgoing edges incident to vertex in this graph
     */
    public Collection<GraphEdge> getOutEdges(GraphNode vertex) {
		return getIncidentEdges(vertex);
	}
    
    /**
     * Returns the number of incoming edges incident to vertex.
     * Equivalent to getInEdges(vertex).size().
     * @param vertex    the vertex whose indegree is to be calculated
     * @return  the number of incoming edges incident to vertex
     */
    public int inDegree(GraphNode vertex) {
		return degree(vertex);
	}
    
    /**
     * Returns the number of outgoing edges incident to vertex.
     * Equivalent to getOutEdges(vertex).size().
     * @param vertex    the vertex whose outdegree is to be calculated
     * @return  the number of outgoing edges incident to vertex
     */
    public int outDegree(GraphNode vertex) {
		return degree(vertex);
	}

    /**
     * Returns the number of predecessors that vertex has in this graph.
     * Equivalent to vertex.getPredecessors().size().
     * @param vertex the vertex whose predecessor count is to be returned
     * @return  the number of predecessors that vertex has in this graph
     */
    public int getPredecessorCount(GraphNode vertex) {
		return degree(vertex);
	}
    
    /**
     * Returns the number of successors that vertex has in this graph.
     * Equivalent to vertex.getSuccessors().size().
     * @param vertex the vertex whose successor count is to be returned
     * @return  the number of successors that vertex has in this graph
     */
    public int getSuccessorCount(GraphNode vertex) {
		return degree(vertex);
	}
    
    /**
     * Returns the vertex at the other end of edge from vertex.
     * (That is, returns the vertex incident to edge which is not vertex.)
     * @param vertex the vertex to be queried
     * @param edge the edge to be queried
     * @return the vertex at the other end of edge from vertex
     */
    public GraphNode getOpposite(GraphNode vertex, GraphEdge edge) {
		Pair<GraphNode> p = getEndpoints(edge);
		if(p.getFirst().equals(vertex)) {
			return p.getSecond();
		}
		else {
			return p.getFirst();
		}
	}
    
    /**
     * Returns all edges that connects v1 to v2.
     * If this edge is not uniquely
     * defined (that is, if the graph contains more than one edge connecting 
     * v1 to v2), any of these edges 
     * may be returned.  findEdgeSet(v1, v2) may be 
     * used to return all such edges.
     * Returns null if v1 is not connected to v2.
     * <br/>Returns an empty collection if either v1 or v2 are not present in this graph.
     *  
     * <p><b>Note</b>: for purposes of this method, v1 is only considered to be connected to
     * v2 via a given <i>directed</i> edge d if
     * v1 == d.getSource() && v2 == d.getDest() evaluates to true.
     * (v1 and v2 are connected by an undirected edge u if 
     * u is incident to both v1 and v2.)
     * 
     * @param v1 The first vertex of the set.
     * @param v2 The second vertex of the set.
     * @return  a collection containing all edges that connect v1 to v2, or null if either vertex is not present
     * @see Hypergraph#findEdge(Object, Object) 
     */
    public Collection<GraphEdge> findEdgeSet(GraphNode v1, GraphNode v2) {
		GraphEdge edge = findEdge(v1, v2);
		if(edge == null) {
			return null;
		}
		
		ArrayList<GraphEdge> ret = new ArrayList<>();
		ret.add(edge);
		return ret;
		
	}
	
    /**
     * Returns true if vertex is the source of edge.
     * Equivalent to getSource(edge).equals(vertex).
     * @param vertex the vertex to be queried
     * @param edge the edge to be queried
     * @return true iff vertex is the source of edge
     */
    public boolean isSource(GraphNode vertex, GraphEdge edge) {
		return getSource(edge).equals(vertex);
	}
    
    /**
     * Returns true if vertex is the destination of edge.
     * Equivalent to getDest(edge).equals(vertex).
     * @param vertex the vertex to be queried
     * @param edge the edge to be queried
     * @return true iff vertex is the destination of edge
     */
    public boolean isDest(GraphNode vertex, GraphEdge edge) {
		return getDest(edge).equals(vertex);
	}
    
    /**
     * Adds edge e to this graph such that it connects 
     * vertex v1 to v2.
     * Equivalent to addEdge(e, new Pair< GraphNode >(v1, v2)).
     * If this graph does not contain v1, v2, 
     * or both, implementations may choose to either silently add 
     * the vertices to the graph or throw an IllegalArgumentException.
     * If edgeType is not legal for this graph, this method will
     * throw IllegalArgumentException.
     * See Hypergraph.addEdge() for a listing of possible reasons
     * for failure.
     * @param e the edge to be added
     * @param v1 the first vertex to be connected
     * @param v2 the second vertex to be connected
     * @param edgeType the type to be assigned to the edge
     * @return true if the add is successful, false otherwise
     * @see Hypergraph#addEdge(Object, Collection)
     * @see #addEdge(Object, Object, Object)
     */
    public boolean addEdge(GraphEdge e, GraphNode v1, GraphNode v2, EdgeType edgeType) {
		//NOTE: Only directed edges allowed
		
		if(edgeType == EdgeType.DIRECTED) {
			throw new IllegalArgumentException();
		}
		
		return addEdge(e, v1, v2);
	}
    
	/**
     * Adds edge to this graph.
     * Fails under the following circumstances:
     * <ul>
     * <li/>edge is already an element of the graph 
     * <li/>either edge or vertices is null
     * <li/>vertices has the wrong number of vertices for the graph type
     * <li/>vertices are already connected by another edge in this graph,
     * and this graph does not accept parallel edges
     * </ul>
     * 
     * @param edge The edge to be added.
     * @param vertices The vertices that should be connected to edge, if present.
     * @return true if the add is successful, and false otherwise
     * @throws IllegalArgumentException if edge or vertices is null, or if a different vertex set in this graph is already connected by edge, or if vertices are not a legal vertex set for edge. 
     */
	@SuppressWarnings("unchecked")
    public boolean addEdge(GraphEdge edge, Collection<? extends GraphNode> vertices) {
		if(edge == null || vertices == null || vertices.size() != 2) {
			return false;
		}
		
		GraphNode[] vs = (GraphNode[])vertices.toArray();
		return addEdge(edge, vs[0], vs[1]);
	}

	/**
     * Adds edge to this graph with type edgeType.
     * Fails under the following circumstances:
     * <ul>
     * <li/>edge is already an element of the graph 
     * <li/>either edge or vertices is null
     * <li/>vertices has the wrong number of vertices for the graph type
     * <li/>vertices are already connected by another edge in this graph,
     * and this graph does not accept parallel edges
     * <li/>edgeType is not legal for this graph
     * </ul>
     * 
     * @param edge The edge to be added.
     * @param vertices The vertices that should be connected to edge, if present.
     * @param edgeType The edge type.
     * @return true if the add is successful, and false otherwise
     * @throws IllegalArgumentException if edge or vertices is null, or if a different vertex set in this graph is already connected by edge, or if vertices are not a legal vertex set for edge 
     */
	@SuppressWarnings("unchecked")
    public boolean addEdge(GraphEdge edge, Collection<? extends GraphNode> vertices, EdgeType edgeType) {
		if(edge == null || vertices == null || vertices.size() != 2) {
			return false;
		}
		
		GraphNode[] vs = (GraphNode[])vertices.toArray();
		return addEdge(edge, vs[0], vs[1], edgeType);
	}
	
	//********************************************************************************
	//   DO NOT EDIT ANYTHING BELOW THIS LINE
	//********************************************************************************
	
	/**
     * Returns a {@code Factory} that creates an instance of this graph type.
     * @param <V> the vertex type for the graph factory
     * @param <E> the edge type for the graph factory
     * @return Returns a Factory that creates an instance of this graph type.
     */
	public static <V,E> Factory<UndirectedGraph<GraphNode,GraphEdge>> getFactory() { 
		return new Factory<UndirectedGraph<GraphNode,GraphEdge>> () {
			@SuppressWarnings("unchecked")
			public UndirectedGraph<GraphNode,GraphEdge> create() {
				return (UndirectedGraph<GraphNode,GraphEdge>) new ThreeTenGraph();
			}
		};
	}
    
    /**
     * Returns the edge type of edge in this graph.
     * @param edge The edge to be classified as undirected.
     * @return the EdgeType of edge, or null if edge has no defined type
     */
    public EdgeType getEdgeType(GraphEdge edge) {
		return EdgeType.UNDIRECTED;
	}
    
    /**
     * Returns the default edge type for this graph.
     * 
     * @return the default edge type for this graph
     */
    public EdgeType getDefaultEdgeType() {
		return EdgeType.UNDIRECTED;
	}
    
    /**
     * Returns the number of vertices that are incident to edge.
     * For hyperedges, this can be any nonnegative integer; for edges this
     * must be 2 (or 1 if self-loops are permitted). 
     * 
     * <p>Equivalent to getIncidentVertices(edge).size().
     * @param edge the edge whose incident vertex count is to be returned
     * @return the number of vertices that are incident to edge.
     */
    public int getIncidentCount(GraphEdge edge) {
		return 2;
	}
	
	/**
	 *  {@inheritDoc}
	 */
	public String toString() {
		return depthFirstTraversal(null, new boolean[MAX_NUMBER_OF_NODES], true);
	}
}
