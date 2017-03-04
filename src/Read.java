/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Trick on 19/05/2016.
 */
public class Read {
    private boolean directed;

    public Graph graph(String src, Graph graph) throws IOException {
        if (src.contains("gml"))
            return gml(src, graph);//gml( src, graph);
        else if (src.contains("csv"))
            return dat(src, graph, ",");
        else
            return dat(src, graph, "\t");

    }

    private Graph gml(String src, Graph graph) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(src)));
        String line = reader.readLine();
        while (line != null) {
            String[] s = line.split("  ");
            String string = s[s.length - 1];

            if (string.contains("node")) {
                while (true) {
                    line = reader.readLine();
                    s = line.split("  ");
                    string = s[s.length - 1];
                    if (string.contains("id")) {
                        graph.add(Integer.parseInt(string.substring(string.indexOf('d') + 2)), new ArrayList<Integer>());
                        break;
                    }
                }
            } else if (string.contains("edge")) {
                find:
                while (true) {
                    line = reader.readLine();
                    s = line.split("  ");
                    string = s[s.length - 1];
                    if (string.contains("source")) {
                        String s1 = string.substring(string.indexOf('e') + 2);
                        while (true) {
                            line = reader.readLine();
                            s = line.split("  ");
                            string = s[s.length - 1];
                            if (string.contains("target")) {
                                graph.addEdge(Integer.parseInt(s1), Integer.parseInt(string.substring(string.indexOf('e') + 3)), 1);
                                break find;
                            }
                        }
                    }
                }
            }


            line = reader.readLine();
        }
        reader.close();
        return graph;
    }

    public Graph dat(String src, Graph graph, String spliter) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(src)));
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Code.name + "\\" + Code.name + ".gml")));
        String line = reader.readLine();
        writer.write("graph\n[\n  directed 0\n");
        Collections.sort(graph.getVertexList());
        while (line != null) {
            String[] s;
//            s = new String[2];
//            int i = 0;
//            while (!Character.isDigit(line.charAt(i))) i++;
//            int j = i;
//            while (Character.isDigit(line.charAt(j))) j++;
//            s[0] = line.substring(i, j);
//            i = j;
//            while (!Character.isDigit(line.charAt(i))) i++;
//            j = i;
//            while (Character.isDigit(line.charAt(j))) j++;
//            s[1] = line.substring(i, j);
            try{
                s = line.split(spliter);
                if (!graph.getVertexList().contains(Integer.parseInt(s[0])) || !graph.getAdjacentVertices(Integer.parseInt(s[0])).contains(Integer.parseInt(s[1])))
                    graph.addEdge(Integer.parseInt(s[0]), Integer.parseInt(s[1]), 1);
            }
            catch (Exception e){
                s = line.split(" ");
                if (!graph.getVertexList().contains(Integer.parseInt(s[0])) || !graph.getAdjacentVertices(Integer.parseInt(s[0])).contains(Integer.parseInt(s[1])))
                    graph.addEdge(Integer.parseInt(s[0]), Integer.parseInt(s[1]), 1);
            };
            line = reader.readLine();
        }
        Collections.sort(graph.getVertexList());
        for (Integer a : graph.getVertexList()) {
            writer.write("  node\n  [\n");
            writer.write("    id " + a + "\n    value " + a + "\n  ]\n");
        }
        for (Integer a : graph.getVertexList())
            for (Integer b : graph.getAdjacentVertices(a)) {
                writer.write("  edge\n  [\n    source " + a + "\n    target " + b + "\n  ]\n");
            }
        writer.write("]");
        writer.close();
        reader.close();
        return graph;
    }

}
