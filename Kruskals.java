import java.io.*;
import java.util.*;

/**
 * This command-line program uses Kruskal's algorithm to find a minimum spanning
 * of a graph. The graph is held in the file named assn9_data.csv as an
 * adjacency list. This program uses the author's DisjSets class without any
 * modification to it. It also uses Java's built-in PriorityQueue class. Each
 * edge of the resultant minimum spanning tree is outputed as the names of two
 * cities and the distance between them. In addition, the sum of all distances
 * is outputed.
 * 
 * * 20FA CS3345 Data Structures and Introduction to Algorithmic Analysis
 * Project #5
 * 
 * @author Gavin John Dunnett
 */
public class Kruskals {

    public Kruskals() {
        PriorityQueue<Edge> pq = inputDataFromFile("assn9_data.csv", ",");
        DisjSets ds = new DisjSets(Vertex.size());
        int sumDistance = 0;
        System.out.printf("%n%15s%15s%9s%n", "City", "City", "Distance");
        System.out.printf("%15s%15s%9s%n", "----", "----", "--------");
        while (!pq.isEmpty()) {
            Edge e = pq.poll(); // mimimum edge
            int root1 = ds.find(e.u.hashCode);
            int root2 = ds.find(e.v.hashCode);
            if (root1 != root2) { 
                ds.union(root1, root2); // edge accepted...
                System.out.printf("%15s%15s%9d%n", e.u, e.v, e.weight);
                sumDistance += e.weight;
            }
        }
        System.out.printf("%n%30s%9d%n", "The sum of all distances is:", sumDistance);
    }

    /**
     * Edge class.
     */
    private static class Edge implements Comparable<Edge> {
        private Vertex u;
        private Vertex v;
        private int weight;

        public Edge(Vertex u, Vertex v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge e) {
            return this.weight < e.weight ? -1 : this.weight == e.weight ? 0 : 1;
        }
    }

    /**
     * Vertex class. With a hashmap, this class implements a custom hashcode that
     * ensures cities with the same name have the same hashcode. In addition,
     * hashcodes start at 0 and increment for each new city. This make these
     * hashcode compatiable with the DisjSets class, which requires int values,
     * starting at 0, for its internal array.
     */
    private static class Vertex {
        private static HashMap<String, Integer> map = new HashMap<>();
        private String city;
        private int hashCode;

        public Vertex(String city) {
            this.city = city;
            if (!map.containsKey(city)) // city not on map...
                map.put(city, map.size()); // so add it to the map
            this.hashCode = map.get(city);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        /**
         * This static method returns the total number of vertices.
         */
        public static int size() {
            return map.size();
        }

        @Override
        public String toString() {
            return city;
        }
    }

    /**
     * Read the input file.
     */
    private PriorityQueue<Edge> inputDataFromFile(String filename, String delimiter) {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        try (Scanner scanner = new Scanner(new FileReader(filename))) {
            while (scanner.hasNextLine()) {
                StringTokenizer line = new StringTokenizer(scanner.nextLine(), delimiter);
                Vertex u = new Vertex(line.nextToken());
                while (line.hasMoreTokens()) {
                    Vertex v = new Vertex(line.nextToken());
                    int weight = Integer.parseInt(line.nextToken());
                    pq.add(new Edge(u, v, weight));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pq;
    }

    public static void main(String[] args) {
        new Kruskals();
    }
}