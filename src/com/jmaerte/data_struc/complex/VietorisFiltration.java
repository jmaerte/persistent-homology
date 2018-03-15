package com.jmaerte.data_struc.complex;

import static com.jmaerte.util.log.Logger.*;

import com.jmaerte.data_struc.point_set.VertexFactory;

import java.util.Arrays;
import java.util.Comparator;

public class VietorisFiltration {
//
//    private VertexFactory factory;
//
//    private Simplex[] simplices;
//    private int fill;
//
//    public VietorisFiltration(VertexFactory factory) {
//        simplices = new Simplex[(int)Math.pow(2, factory.size())];
//        Simplex max = new Simplex(factory);
//        this.factory = factory;
//        simplices[simplices.length - 1] = max;
//        fill++;
//        generate(max, 0);
//        sort();
//        log("Done");
////        info(Arrays.toString(simplices));
//    }
//
//    private void generate(Simplex curr, int k) {
//        double valuation = 0;
//        // O(k*n)
//        for(int j = 0; j < k; j++) {
//            Simplex s = simplices[simplices.length - 1];
//            if(curr.dim() == 0) {
//                while(s.dim() != -1) {
//                    s = s.getFace(0);
//                }
//            }else {
//                int n = 0, m = 0;
//                while(s.dim() > curr.dim() - 1) {
//                    if(n == j) n++;
//                    if(n <= curr.dim() && s.get(m) == curr.get(n)) {
//                        m++;
//                        n++;
//                    }else {
//                        s = s.getFace(m);
//                    }
//                }
//            }
//            curr.addFace(s);
//            if(s.getValuation(filtrationID) > valuation) {
//                valuation = s.getValuation(filtrationID);
//            }
//            // walk from max to the element curr\{curr.vertices[j]}
//        }
//        for(int i = k; i < curr.dim() + 1; i++) {
//            Simplex face = new Simplex(curr, i);
//            curr.addFace(face);
//            simplices[simplices.length - (fill++) - 1] = face;
//            generate(face, i);
//
//            if(face.dim() == 1) {
//                face.setValuation(filtrationID, factory.d(face.get(0), face.get(1)));
//            }
//            if(face.getValuation(filtrationID) > valuation) {
//                valuation = face.getValuation(filtrationID);
//            }
//        }
//        curr.setValuation(filtrationID, valuation);
//        log("One Simplex of dimension " + curr.dim() + " done.");
//    }
//
//
//    private void generate(Simplex curr, Simplex parent, int k) {
//        System.out.println("previous: " + curr + " " + parent + " " + k);
//
//        double valuation = -1;
//        for(int j = 0; j < k; j++) {
//            Simplex prev = parent.getFace(j);
//            curr.addFace(prev.getFace(k - j - 1));
//            if(prev.getFace(k - j - 1).getValuation(filtrationID) > valuation) {
//                valuation = prev.getFace(k - j - 1).getValuation(filtrationID);
//            }
//            // Search in the whole tree under parent for the element that would be thrown away here.
//            // link i-th face of the parent to
//        }
//        for(int i = k; i < curr.dim() + 1; i++) {
////            System.out.println("next: " + curr + " " + parent + " " + i);
//            Simplex face = new Simplex(curr, i);
//            curr.addFace(face);
//            simplices[simplices.length - (fill++) - 1] = face;
//            generate(face, curr, i);
//
//            if(face.dim() == 1) {
//                face.setValuation(filtrationID, factory.d(face.get(0), face.get(1)));
//            }
//            if(face.getValuation(filtrationID) > valuation) {
//                valuation = face.getValuation(filtrationID);
//            }
//        }
//        curr.setValuation(filtrationID, valuation);
//        log("One Simplex of dimension " + curr.dim() + " done.");
//    }
//
//    public void sort() {
//        log("Sorting");
//        Arrays.sort(simplices, new Comparator<Simplex>() {
//            @Override
//            public int compare(Simplex o1, Simplex o2) {
//                double delta = o1.getValuation(filtrationID) - o2.getValuation(filtrationID);
//                if(delta == 0) return o1.dim() - o2.dim();
//                else if(delta < 0) return -1;
//                else return 1;
//            }
//        });
//    }
//
//    public class Node {
//        private Simplex s;
//        private int index;
//
//    }
}
