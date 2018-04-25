package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.calc.Function;

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

}
