
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;


public class Graph{


    private HashMap<Integer, ArrayList<Integer>> adjacencyList;

    /**
     * This list holds all the vertices so that we can iterate over them in the
     * <p>
     * toString function
     */

    private ArrayList<Integer> vertexList;


    private boolean directed;


    public Graph(boolean isDirected) {

        directed = isDirected;

        adjacencyList = new HashMap<>();

        vertexList = new ArrayList();

    }

    public Graph(Graph o) {

        directed = o.directed;

        adjacencyList = new HashMap<>(o.adjacencyList);

        vertexList = new ArrayList(o.vertexList);

    }

    public void del(Integer x){
        for(Integer i : this.getAdjacentVertices(x)){
            this.adjacencyList.get(i).remove(x);
        }
        this.vertexList.remove(x);
    }


    public void add(Integer vertex, ArrayList<Integer> connectedVertices) {

        // Add the new vertex to the adjacencyList with it's list of connected

        // nodes

        adjacencyList.put(vertex, connectedVertices);

        vertexList.add(vertex);

        // If this is an undirected graph, every edge needs to represented

        // twice, once in the added vertex's list and once in the list of each

        // of the vertex's connected to the added vertex


        for (Integer vertexConnectedToAddedVertex : connectedVertices) {

            ArrayList<Integer> correspondingConnectedList = adjacencyList

                    .get(vertexConnectedToAddedVertex);

            // The added vertex's connections might not be represented in

            // the Graph yet, so we implicitly add them

            if (correspondingConnectedList == null) {

                adjacencyList.put(vertexConnectedToAddedVertex,

                        new ArrayList<Integer>());

                vertexList.add(vertexConnectedToAddedVertex);

                correspondingConnectedList = adjacencyList

                        .get(vertexConnectedToAddedVertex);

            }


            if (!directed) {

                // The weight from one vertex back to another in an undirected

                // graph is equal


                correspondingConnectedList.add(vertex);

            }

        }


    }

    public boolean addEdge(Integer vertexOne, Integer vertexTwo, int weight) {

        if (directed) {

            return false;

        }


        if (!adjacencyList.containsKey(vertexOne)) {

            ArrayList<Integer> tempList = new ArrayList<Integer>();

            tempList.add(vertexTwo);

            add(vertexOne, tempList);

            return true;

        }


        if (!adjacencyList.containsKey(vertexTwo)) {

            ArrayList<Integer> tempList = new ArrayList<Integer>();

            tempList.add(vertexOne);

            add(vertexTwo, tempList);

            return true;

        }


        adjacencyList.get(vertexOne).add(vertexTwo);

        adjacencyList.get(vertexTwo).add(vertexOne);

        return true;

    }


    /**
     * This method returns a list of all adjacent vertices of the give vertex without weight
     *
     * @param vertex the source vertex
     * @return an array list containing the vertices
     */

    public ArrayList<Integer> getAdjacentVertices(Integer vertex) {

        ArrayList<Integer> returnList = new ArrayList();

        for (Integer edge : adjacencyList.get(vertex)) {

            returnList.add(edge);

        }

        return returnList;

    }

    public ArrayList<Integer> get2AdjacentVertices(Integer vertex) {

        ArrayList<Integer> returnList = new ArrayList(getAdjacentVertices(vertex));

        for (Integer vert : getAdjacentVertices(vertex))
            for (Integer edge : adjacencyList.get(vert)) {
                if (!returnList.contains(edge) && !edge.equals(vertex))
                    returnList.add(edge);

            }
        Collections.sort(returnList);
        return returnList;
    }

    public boolean hasEdge(Integer source, Integer end){
        for (Integer edge : adjacencyList.get(source)) {
            if (edge.equals(end)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Integer> getVertexList() {

        return vertexList;

    }

    public String toString() {

        String s = "";

        for (Integer vertex : vertexList) {

            s += vertex.toString();

            s += " : ";

            s += adjacencyList.get(vertex);

            s += "\n";

        }

        return s;

    }


}
