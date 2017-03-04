/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by Trick on 30/08/2016.
 */
public class SIR_20170102 {
    private Graph o = null;
    private double mu, beta;
    private int tMax, iteration;

    private int size;
    int m = 0;
    private final JFrame frame;
    private final JLabel counter = new JLabel();
    private final JTextPane detail = new JTextPane();
    private final JPanel panel = new JPanel();
    private final JProgressBar progressBar = new JProgressBar();


    public SIR_20170102(Graph o, JFrame frame, double mu, double beta, int tMax, int iteration) {
        this.o = o;
        this.frame=frame;
        this.mu = mu;
        this.beta = beta;
        this.tMax = tMax;
        this.iteration = iteration;

        size = o.getVertexList().size();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 250);
        frame.setVisible(true);
        panel.setLayout(new BorderLayout());
        counter.setHorizontalAlignment(JLabel.CENTER);
        panel.add(counter, BorderLayout.NORTH);
        frame.add(panel);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        Border border = BorderFactory.createTitledBorder("Progress...");
        progressBar.setBorder(border);
        panel.add(progressBar, BorderLayout.SOUTH);
        panel.add(detail, BorderLayout.CENTER);
        detail.setEditable(false);
        detail.setAlignmentY(JTextPane.CENTER_ALIGNMENT);
    }

    public double[][] compute(int infectedNode) {
        int[][] infectionMatrix,recoveryMatrix;
        infectionMatrix = new int[tMax][iteration];
        recoveryMatrix = new int[tMax][iteration];
        if (!o.getVertexList().contains(0))
            m = 1;
        StringBuilder builder = new StringBuilder();
        builder.append("Starting SIR For Node " + infectedNode);
        builder.append("\nGraph Size : " + size);
        builder.append("\nMu : " + mu);
        builder.append("\nbeta : " + beta);
        builder.append("\ntmax : " + tMax);
        builder.append("\niteration : " + iteration);
        detail.setText(builder.toString());
        counter.setText("0%");
        int u = 0;
        long startTime = System.currentTimeMillis();
        for (int n = 0; n < iteration; n++) {
            if ((int) (100 * (double) (n + 1) / (iteration)) > u) {
//                a = (int)(100*(double) (n+1)/(iteration));
                long Time = System.currentTimeMillis();
                u++;
                int h = (int) ((int) (100 - u) * (Time - startTime) / 1000 / u / 3600);
                int m = (int) ((int) (100 - u) * (Time - startTime) / 1000 / u / 60) % 3600;
                int s = (int) ((int) (100 - u) * (Time - startTime) / 1000 / u) % 60;
                counter.setText(" Remaining : " + h + ":" + m + ":" + s);
                progressBar.setValue(u);
            }
            int [] infectedList,susceptible,recovered,oldInfectedList;
            infectedList = new int[size];
            susceptible = new int[size];
            recovered = new int[size];
            oldInfectedList = new int[size];
            for (int p = 0; p < size; p++) {
                infectedList[p]=0;
                susceptible[p]=1;
                recovered[p]=0;
            }
            infectedList[infectedNode]=1;
            susceptible[infectedNode]=0;
            for (int t = 1; t < tMax; t++) {
                for (int j = 0; j < size; j++) {
//                    System.out.println("j = " + j + " : " + infectedList[j] + " res : " + (infectedList[j]==1));
                    if (susceptible[j]==1) {

                        int ni = 0;
                        for (Integer k : o.getAdjacentVertices(j + m)) {
                            if (oldInfectedList[k-m]==1) {
                                ni++;
                            }
                        }
                        if (Math.random() < (1-Math.pow(1-beta,ni))) {
                            infectedList[j]=1;
                            susceptible[j]=0;
//                                    System.out.println(k + " infected\n");
                        } else if(oldInfectedList[j]==1)
                            if (Math.random() < mu) {
                                //                            System.out.println(j+1 + "recovered\n");
                                infectedList[j]=0;
                                susceptible[j]=0;
                                recovered[j]=1;
                            }
                    }
                }
                for (int p = 0; p < size; p++) {
                    infectionMatrix[t][n] += infectedList[p];
                    recoveryMatrix[t][n] += recovered[p];
                }
            }
        }
        System.out.println("=");
        double[] infectionRate = new double[tMax];
        double[] recoveryRate = new double[tMax];
        for (int p = 0; p < tMax; p++) {
            int w = 0;
            int q = 0;

            for (int l = 0; l < iteration; l++) {
                w += infectionMatrix[p][l];
                q += recoveryMatrix[p][l];
            }
            infectionRate[p] = (double) w / iteration;
            recoveryRate[p] = (double) q / iteration;
        }
        builder.append("\nFinished SIR For " + infectedNode);
        detail.setText(builder.toString());
        double [][] x = new double[2][tMax];
        x[0] = infectionRate;
        x[1]=recoveryRate;
        return x;
    }
}
