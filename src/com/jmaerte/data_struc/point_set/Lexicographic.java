package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.calc.Function;
import com.jmaerte.util.input.Writer;

import java.io.BufferedWriter;
import java.util.ArrayList;

public class Lexicographic implements Function<Lexicographic, Double> {

    private String s;

    private Lexicographic() {}

    public static Lexicographic fromString(ArrayList<String> s) {
        Lexicographic l = new Lexicographic();
        l.s = s.get(0);
        return l;
    }

    public Double eval(Lexicographic l) {
        return Metric.Levehnshtein.d(this.s, l.s);
    }

    public void write(BufferedWriter bw) throws Exception {
        bw.write(s);
    }
}
