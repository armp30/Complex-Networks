import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Trick on 19/05/2016.
 */
public class Code {
    public Graph o = new Graph(false);
    private HashMap<Integer, ArrayList<Integer>> N2 = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> N1 = new HashMap<>();
    private HashMap<Integer, Double> cc = new HashMap<>();
    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(name + "\\" + name + "Time.txt")));

    public Code() throws IOException {
        KendallsCorrelation kendallsCorrelation = new KendallsCorrelation();

        Read v = new Read();
        o = v.graph((dir.length() >= 1 ? dir + "\\" : "") + name + format, o);
        k();
        long timeMillis = System.currentTimeMillis();
        Collections.sort(o.getVertexList());
        reOrder re = new reOrder();
        re.reOrder1(o);
        for (Integer a : o.getVertexList()) {
//            System.out.println(a);
            N1.put(a, o.getAdjacentVertices(a));
            N2.put(a, o.get2AdjacentVertices(a));
        }

        timeMillis = System.currentTimeMillis() - timeMillis;

        writer.write("making list = " + Long.toString(timeMillis / 1000) + " s" + System.lineSeparator());
        cc();
        timeMillis = System.currentTimeMillis() - timeMillis;

        writer.write("cc list = " + Long.toString(timeMillis / 1000) + " s" + System.lineSeparator());
    }

    public ArrayList<Integer> getN2(int i) {
        return N2.get(i);
    }

    public ArrayList<Integer> getN1(int i) {
        return N1.get(i);
    }

    public void cc() {
        for (Integer a : o.getVertexList()) {
            int k = 0;
            for (Integer y : getN1(a)) {
                for (Integer p : getN1(a)) {
                    if (o.hasEdge(y, p))
                        k++;
                }
            }
            int c = getN1(a).size();
            if (c > 1) {
                cc.put(a, (double) k / (c * (c - 1)));
            } else {
                cc.put(a, 0.0);
            }
        }
    }

    public double ccN(int i) {
        double r = 0;

        for (Integer x : getN2(i)) {
            if (!getN1(i).contains(x)) {
                r += cc.get(x);
            }
        }

        return r;
    }

    //LC

    public int Q(int i) {
        int p = 0;
        for (Integer x : getN1(i)) {
            p += getN2(x).size();
        }
        return p;
    }

    public double cl(int i) {
        double p = 0;
        for (Integer x : getN1(i)) {
            p += Q(x);
        }
        return p;
    }

    //DIL

    public double I(int i, int j) {
        ArrayList<Integer> x = getN1(i);
        ArrayList<Integer> y = getN1(j);
        int km = x.size(), kn = y.size();
        int p = 0;
        for (int c : x)
            for (int v : y)
                if (c == v)
                    p++;
        int u = (km - p - 1) * (kn - p - 1);
        double l = (double) p / 2 + 1;
        return (double) u / l;
    }

    public double W(int i, int j) {
        ArrayList<Integer> x = getN1(i);
        ArrayList<Integer> y = getN1(j);
        int km = x.size(), kn = y.size();
        return I(i, j) * (km - 1) / (km + kn - 2);
    }

    public double L(int i) {
        double w = 0;
        for (int k : getN1(i)) {
            w += W(i, k);
        }
        return (double) getN1(i).size() + w;
    }

    //Ecc

    public double Ecc(int i, int j) {
        ArrayList<Integer> x = getN1(i);
        ArrayList<Integer> y = getN1(j);
        int km = x.size(), kn = y.size();
        int p = 0;
        for (int c : x)
            for (int v : y)
                if (c == v)
                    p++;
        if(p>0)
            return (double) p / (km > kn ? kn - 1 : km - 1);
        return 0;
    }


    public double cls(int i, double j) {
        double k = 0;
        for (Integer x : getN1(i)) {
            double r = 0;
            for (Integer y : getN2(i)) {
                r += cc.get(y);
            }
            k += (j * getN2(x).size() + ((1 - j) * r));
        }
        return k;
    }


    public HashMap<Integer, Double> clcompute() {
        HashMap<Integer, Double> aq = new HashMap<>();
        for (Integer x : o.getVertexList()) {
            aq.put(x, cl(x));
        }
        return aq;
    }

    public HashMap<Integer, Double> clscompute(double a) {
        HashMap<Integer, Double> aq = new HashMap<>();
        for (Integer x : o.getVertexList()) {
            aq.put(x, cls(x,a));
        }
        return aq;
    }

    public HashMap<Integer, ArrayList<Integer>> kshel() {

        kshell kl = new kshell(o);
        HashMap<Integer, ArrayList<Integer>> op = kl.getData();
        return op;
    }



    public HashMap<Integer, Double> degree() throws IOException {
        HashMap<Integer, Double> x = new HashMap<>();
        long timeMillis = System.currentTimeMillis();
        for (Integer i : o.getVertexList()) {
//            System.out.println("proposed " + i);
            x.put(i, (double) o.getAdjacentVertices(i).size());
        }
        timeMillis = System.currentTimeMillis() - timeMillis;

        writer.write("Degree Final list = " + Long.toString(timeMillis));
        return x;
    }

    public HashMap<Integer, Double> L() throws IOException {
        HashMap<Integer, Double> x = new HashMap<>();
        long timeMillis = System.currentTimeMillis();
        for (Integer i : o.getVertexList()) {
            x.put(i, L(i));
        }
        timeMillis = System.currentTimeMillis() - timeMillis;

        writer.write("L Final list = " + Long.toString(timeMillis));
        return x;
    }


    public int Rank(ArrayList<Double> w, HashMap<Integer, Double> z, String dst, boolean d) throws IOException {
        System.out.println("Ranking " + dst.substring(2 * name.length() + 1, dst.indexOf("Rank")));
        BufferedWriter wr = new BufferedWriter(new FileWriter(new File(dst)));
        Collections.sort(w);
        int r = 0;
        if (!d)
            Collections.reverse(w);
        for (int i = 0; i < w.size(); i++) {
            for (Integer j : z.keySet()) {
                if (w.get(i) != null && w.get(i) == z.get(j)) {
                    if (i == 0)
                        r = j;
//                    System.out.println(j + "\t" + z.get(j));
                    wr.write(1 + i + "," + j + "," + z.get(j) + System.lineSeparator());
                }
            }
        }
        wr.close();
        System.out.println("Ranking Finished");
        return r;
    }


    public void k() throws IOException {
        BufferedWriter writer1 = new BufferedWriter(new FileWriter(new File(name + "\\" + name + "K.txt")));
        double k = 0.0;
        double k2 = 0.0;
        for (Integer a : o.getVertexList()) {
            k += o.getAdjacentVertices(a).size();
        }
        k /= o.getVertexList().size();
        writer1.write("<k> = " + k + System.lineSeparator());
        for (Integer a : o.getVertexList()) {
            k2 += o.getAdjacentVertices(a).size() * o.getAdjacentVertices(a).size();
        }
        k2 /= o.getVertexList().size();
        writer1.write("<k2> = " + k2 + System.lineSeparator());
        writer1.write("<B> = " + k / k2 + System.lineSeparator());
        writer1.close();
    }


    static String dir = Data.dir;
    static String name = Data.name;
    static String format = Data.format;


    public static void main(String[] args) throws IOException {


        File theDir = new File(name);
        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
            }
        }

        Code a = new Code();
        JFrame frame = new JFrame();
        SIR sir20170102 = new SIR(a.o,frame, Data.mu, Data.beta, Data.tmax, Data.iteration);
        SIR2 sir2 = new SIR2(a.o,frame, Data.mu, Data.beta, Data.tmax, Data.iteration);
        HashMap<Integer, Double> z;
        ArrayList<Double> w;


        Degree(a);
//        Bridge(a);
//
//        Local(a);
//
//        KShell(a);

//        BridgeC(a);
//
//        sir20170102(sir20170102, sir2, "Degree", "1st", Degree(a));
//        sir20170102(sir20170102, sir2, "Degree", "1st", Local(a));
//        sir20170102(sir20170102, sir2, "Degree", "1st", Bridge(a));
//        sir20170102(sir20170102, sir2, "BridgC", "1st", BridgeC(a));
        frame.dispose();
        a.writer.close();

    }


    private static int Degree(Code a) throws IOException {
        HashMap<Integer, Double> z;
        ArrayList<Double> w;
        String method = "Degree";
        Name(method);
        BufferedWriter write = new BufferedWriter(new FileWriter(new File(name + "\\" + name + method + "Resault.txt")));
        z = a.degree();
        w = new ArrayList<>();
        for (Integer i : z.keySet()) {
            w.add(z.get(i));
            write.write(i + "\t" + z.get(i) + System.lineSeparator());
        }
        write.close();
        return a.Rank(w, z, name + "\\" + name + method + "Rank.txt", false);
    }

    private static int Bridge(Code a) throws IOException {
        HashMap<Integer, Double> z;
        ArrayList<Double> w;
        String method = "Bridge";
        Name(method);
        BufferedWriter writ = new BufferedWriter(new FileWriter(new File(name + "\\" + name + method + "Resault.txt")));
        z = a.L();
        w = new ArrayList<>();
        for (Integer i : z.keySet()) {
            w.add(z.get(i));
            writ.write(i + "\t" + z.get(i) + System.lineSeparator());
        }
        writ.close();
        return a.Rank(w, z, name + "\\" + name + method + "Rank.txt", false);
    }

    private static int Local(Code a) throws IOException {
        HashMap<Integer, Double> z;
        ArrayList<Double> w;
        String method = "Local";
        Name(method);
        BufferedWriter wri = new BufferedWriter(new FileWriter(new File(name + "\\" + name + method + "Resault.txt")));
        z = a.clcompute();
        w = new ArrayList<>();
        for (Integer i : z.keySet()) {
            w.add(z.get(i));
            wri.write(i + "\t" + z.get(i) + System.lineSeparator());
        }
        wri.close();
        return a.Rank(w, z, name + "\\" + name + method + "Rank.txt", false);
    }

    private static int LocalStructure(Code a) throws IOException {
        HashMap<Integer, Double> z;
        ArrayList<Double> w;
        String method = "LocalStructure";
        Name(method);
        BufferedWriter wri = new BufferedWriter(new FileWriter(new File(name + "\\" + name + method + "Resault.txt")));
        z = a.clscompute(0.2);
        w = new ArrayList<>();
        for (Integer i : z.keySet()) {
            w.add(z.get(i));
            wri.write(i + "\t" + z.get(i) + System.lineSeparator());
        }
        wri.close();
        return a.Rank(w, z, name + "\\" + name + method + "Rank.txt", false);
    }

    private static void KShell(Code a) throws IOException {
        String method = "Kshell";
        Name(method);
        BufferedWriter wr = new BufferedWriter(new FileWriter(new File(name + "\\" + name + method + ".txt")));
        HashMap<Integer, ArrayList<Integer>> op = a.kshel();
        for (Integer v : op.keySet()) {
            wr.write(v + " << ");
            for (Integer i : op.get(v)) {
                wr.write(i + " ");
            }
            wr.write(System.lineSeparator());
        }
        wr.close();
    }

    //----------------------------------------------------------------

    private static void Name(String method) {
        System.out.println("\nExecuting " + method + " Centrality");
    }

    private static double[][] sir(SIR_20170102 sir, SIR2_20170102 sir2, String method, String num, int infectedNode) throws IOException {
        double[][] res;
        try {
            res = sir.compute(infectedNode);
        } catch (IndexOutOfBoundsException e) {
            res = sir2.compute(infectedNode);

        }
        File theDir = new File(name + "\\SIR");
        if (!theDir.exists()) {
            try {
                theDir.mkdir();
            } catch (SecurityException se) {
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                new File(name + "\\SIR\\" + method + num + infectedNode + "infection.csv")));
        BufferedWriter writer1  = new BufferedWriter(new FileWriter(
                new File(name + "\\SIR\\" + method + num + infectedNode + "recovery.csv")));

        for (int i = 0; i < res[0].length; i++) {
            writer.write(i + "," + res[0][i] + "\n");
            writer1.write(i + "," + res[1][i] + "\n");
        }
        writer.close();
        return res;
    }
}
