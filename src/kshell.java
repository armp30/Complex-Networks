import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Trick on 09/07/2016.
 */

public class kshell {
    public HashMap<Integer, ArrayList<Integer>> getData() {
        return data;
    }

    HashMap<Integer, ArrayList<Integer>> data = new HashMap<>();

    public kshell(Graph z) {
        Graph o = new Graph(z);
        int k = 1;
        int n = 0;
        while (!o.getVertexList().isEmpty()) {
            System.out.println("\"" + k + "\"");
            ArrayList<Integer> a = new ArrayList<>();
            ArrayList<Integer> s = new ArrayList<>(o.getVertexList());
            for (Integer e : s) {
                for (Integer x : s) {
                    System.out.println(x + " : " + o.getAdjacentVertices(x));
                    if (!o.getVertexList().isEmpty())

                        if (o.getAdjacentVertices(x).size() <= k && !a.contains(x)) {
                            a.add(x);
                            o.del(x);
                            n++;
                        }
                }
            }
            data.put(k++, a);
        }
        System.out.println("------------ " + n + " -----------");
    }

}
