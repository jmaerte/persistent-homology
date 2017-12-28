package com.jmaerte.simplicial;

import com.jmaerte.lin_alg.Vector;
import com.jmaerte.util.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Julian on 23/12/2017.
 */
public class Filtration<V extends Vector<V>> {

    CIndexList<Simplex<V>> complex;//Compatible ordering
    IndexList<Integer> filtration;

    private Filtration(CIndexList<Simplex<V>> complex, Function<Simplex, Double> f) {
        this.complex = complex;
        filtration = new IndexList<>(Integer[].class);
        double val = f.eval(complex.list[0]);
        filtration.add(-1);
        for(int i = 0; i < complex.occupation(); i++) {
            if(f.eval(complex.list[i]) > val) {
                filtration.add(i-1);
            }
        }
        filtration.add(complex.occupation - 1);
    }


    public static Filtration function_fromFile(String path) {
        Function<Simplex, Double> f = null;
        int size = 16;//count size of the komplex before creating it.
        CIndexList<Simplex> complex = new CIndexList<>(Simplex[].class, size, new Comparator<Simplex>() {
            @Override
            public int compare(Simplex o1, Simplex o2) {
                int sign = f.eval(o1) - f.eval(o2) < 0 ? -1 : 1;
                return sign*(int) Math.ceil(Math.abs(f.eval(o1) - f.eval(o2)));
            }
        });
        return new Filtration(complex, f);
    }

    public static <V extends Vector<V>> Filtration<V> cech_fromVertices(int[] vertexIDs, VertexFactory<V> factory, Metric<V> d) {
        int size = 16;//count size of the komplex before creating it.
        ArrayList<Simplex<V>> complex = new ArrayList<>();
        ArrayList<Double> radii = new ArrayList<>();
        // TODO: Fill complex as the power-complex of the simplices defined through vertexIDs. While generating the complex for each simplex calculate the smallest enclosing ball of the vertices of it. Then fill a IndexList with this simplices and compare by radius of the SEB. If radius changes from one to another simplex mark this as filtration point.
        Shifter shifter = new Shifter(vertexIDs.length);
        SEB seb;
        for(int k = 1; k <= vertexIDs.length; k++) {
            shifter.reset(k);
            while(!shifter.isMax()) {
                System.out.println(shifter.get());
                complex.add(shifter.wrap(vertexIDs, factory, d));
                shifter.shift();
            }
        }
        Function<Simplex<V>, Double> f = null;
        // TODO: Create Function out of the radii and simplices. After sort simplices for their smallest enclosing ball radius through adding it to the CIndexList(Comparator will compare the radii of the input simplices).
        CIndexList<Simplex<V>> sort_Complex = new CIndexList<Simplex<V>>(Simplex<V>[].class, size, new Comparator<Simplex>() {
            @Override
            public int compare(Simplex o1, Simplex o2) {
                int sign = f.eval(o1) - f.eval(o2) < 0 ? -1 : 1;
                return sign*(int) Math.ceil(Math.abs(f.eval(o1) - f.eval(o2)));
            }
        });
        for(Simplex<V> s : complex) {
            sort_Complex.add(s);
        }
        return new Filtration<V>(sort_Complex, f);
    }

    public static Filtration cech_fromFile(String path) {
        return null;
    }


    public Homology persistent() {

    }
}
