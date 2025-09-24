import java.util.*;

public class AmbulanceRouter {

    // --- 1. Graph Representation ---

    // Represents a road (edge) connecting two intersections
    static class Edge {
        int targetNode;
        int weight; // Travel time in minutes

        Edge(int targetNode, int weight) {
            this.targetNode = targetNode;
            this.weight = weight;
        }
    }

    // Represents the state in our priority queue: distance to a specific node
    static class NodeDistance implements Comparable<NodeDistance> {
        int nodeId;
        int distance;

        NodeDistance(int nodeId, int distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    // --- 2. Dijkstra's Algorithm Implementation ---

    /**
     * Finds the shortest paths from a start node to all other nodes in the graph.
     * @param graph The city's road network.
     * @param startNode The ambulance's starting intersection.
     * @return A map linking each node to its predecessor in the shortest path.
     */
    public static Map<Integer, Integer> findShortestPaths(Map<Integer, List<Edge>> graph, int startNode, Map<Integer, Integer> distances) {
        // Priority queue to always process the node with the smallest distance first
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();

        // Map to reconstruct the path later
        Map<Integer, Integer> predecessors = new HashMap<>();

        // Initialize distances to infinity for all nodes
        for (Integer node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        
        // The distance from the start node to itself is 0
        distances.put(startNode, 0);
        pq.add(new NodeDistance(startNode, 0));
        predecessors.put(startNode, null); // The start node has no predecessor

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            int u = current.nodeId;
            int dist_u = current.distance;

            // If we've found a shorter path already, skip this one
            if (dist_u > distances.get(u)) {
                continue;
            }

            // Explore neighbors (adjacent intersections)
            if (graph.get(u) == null) continue; // Node with no outgoing roads
            
            for (Edge edge : graph.get(u)) {
                int v = edge.targetNode;
                int weight_uv = edge.weight;
                
                // Relaxation step: If we found a shorter path to v through u
                if (distances.get(u) + weight_uv < distances.get(v)) {
                    distances.put(v, distances.get(u) + weight_uv);
                    pq.add(new NodeDistance(v, distances.get(v)));
                    predecessors.put(v, u);
                }
            }
        }
        return predecessors;
    }

    /**
     * Reconstructs the path from the start to the end node.
     */
    public static List<Integer> getPath(Map<Integer, Integer> predecessors, int endNode) {
        LinkedList<Integer> path = new LinkedList<>();
        Integer at = endNode;
        while (at != null) {
            path.addFirst(at);
            at = predecessors.get(at);
        }
        // If the path doesn't start with the source, it's unreachable
        if (path.isEmpty() || !predecessors.containsKey(path.getFirst())) {
            return Collections.emptyList();
        }
        return path;
    }


    // --- 3. Main Method: Demonstration ---
    public static void main(String[] args) {
        // The city map: An adjacency list
        Map<Integer, List<Edge>> cityGraph = new HashMap<>();
        // Helper to add roads
        for (int i = 1; i <= 8; i++) cityGraph.put(i, new ArrayList<>());

        // Define the road network (node1, node2, time)
        cityGraph.get(1).add(new Edge(2, 5));
        cityGraph.get(1).add(new Edge(3, 9));
        cityGraph.get(2).add(new Edge(1, 5));
        cityGraph.get(2).add(new Edge(4, 3));
        cityGraph.get(3).add(new Edge(1, 9));
        cityGraph.get(3).add(new Edge(5, 4));
        cityGraph.get(4).add(new Edge(2, 3));
        cityGraph.get(4).add(new Edge(5, 1));
        cityGraph.get(4).add(new Edge(6, 7));
        cityGraph.get(5).add(new Edge(3, 4));
        cityGraph.get(5).add(new Edge(4, 1));
        cityGraph.get(5).add(new Edge(7, 2));
        cityGraph.get(6).add(new Edge(4, 7));
        cityGraph.get(6).add(new Edge(8, 3));
        cityGraph.get(7).add(new Edge(5, 2));
        cityGraph.get(7).add(new Edge(8, 6));
        cityGraph.get(8).add(new Edge(6, 3));
        cityGraph.get(8).add(new Edge(7, 6));

        int ambulanceStart = 1;
        int[] hospitals = {7, 8};

        System.out.println("--- Initial Scenario: Normal Traffic ---");
        findAndPrintBestRoute(cityGraph, ambulanceStart, hospitals);

        System.out.println("\n======================================================\n");

        System.out.println("--- DYNAMIC UPDATE: Major congestion on road 5 -> 7 ---");
        // Update the weight of the edge from intersection 5 to 7
        for(Edge edge : cityGraph.get(5)){
            if(edge.targetNode == 7){
                edge.weight = 20; // Travel time increases from 2 to 20 mins!
            }
        }
        findAndPrintBestRoute(cityGraph, ambulanceStart, hospitals);
    }
    
    private static void findAndPrintBestRoute(Map<Integer, List<Edge>> graph, int start, int[] destinations) {
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> predecessors = findShortestPaths(graph, start, distances);
        
        int bestDestination = -1;
        int minTime = Integer.MAX_VALUE;

        System.out.println("Calculating routes to all possible hospitals...");
        for (int hospital : destinations) {
            int time = distances.get(hospital);
            System.out.printf("Time to Hospital at intersection %d: %d minutes\n", hospital, time);
            if (time < minTime) {
                minTime = time;
                bestDestination = hospital;
            }
        }
        
        System.out.println("\n✅ Optimal Choice:");
        System.out.printf("Nearest hospital is at intersection %d, which takes %d minutes.\n", bestDestination, minTime);
        
        List<Integer> path = getPath(predecessors, bestDestination);
        System.out.println("Recommended Path: " + path);
    }
}
