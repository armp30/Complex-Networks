/**
 * Created by Trick on 19/05/2016.
 */
public class Edge {
    private Integer vertex;
    private int weight;
    public Edge(Integer vert,int w){
        vertex = vert;
        weight = w;
    }

    public Integer getVertex() {
        return vertex;
    }

    public void setVertex(Integer vertex) {
        this.vertex = vertex;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "( " + vertex + ", " + weight + " )";
    }
}
