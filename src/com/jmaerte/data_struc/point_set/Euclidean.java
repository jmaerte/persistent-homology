package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.log.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Euclidean implements PointSet {

    private int currID;
    private PointSet set;
    private ScalarProduct q;

    public Euclidean(PointSet set, ScalarProduct q) {
        assert set.dimension() == q.dimension();
        this.set = set;
        currID = set.size();
        this.q = q;
    }

    public double d(Integer i, Integer j) {
        return d(set.get(i), set.get(j));
    }

    public double d(double[] a, double[] b) {
        return q.d(a, b);
    }

    public double q(double[] a, double[] b) {
        return q.scalar(a, b);
    }

    public int size() {
        return currID;
    }

    public String toString() {
        return set.toString();
    }

    public int dimension() {
        return set.dimension();
    }

    public double get(int i, int j) {
        return set.get(i, j);
    }

    public double[] get(int i) {
        return set.get(i);
    }

    public String toPlot() {
        return set.toPlot();
    }

    public void toFile() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd - HH mm ss");
        File file = new File("output/" + dateFormat.format(Logger.date) + "/");
        file.mkdirs();
        File ps = new File("output/" + dateFormat.format(Logger.date) + "/pointset.txt");
        try {
            ps.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(ps));
            for(int i = 0; i < size(); i++) {
                for(int j = 0; j < dimension(); j++) {
                    bw.write((j != 0 ? " ": "") + get(i, j));
                }
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
