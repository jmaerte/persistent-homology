package com.jmaerte.data_struc.complex;

import com.jmaerte.data_struc.miniball.AffineHull;
import com.jmaerte.data_struc.miniball.Ball;
import com.jmaerte.data_struc.miniball.Miniball;
import com.jmaerte.data_struc.point_set.Euclidean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Julian on 26/03/2018.
 */
public class CechFiltration extends Filtration {

    private double eps = 1e-10;
    private Euclidean S;
    private ArrayList<Simplex> simplices;
    private Integer[] sigma;
    private int[] tau;
    private int k;
    private int dim;

    public CechFiltration(Euclidean S, int k) {
        this.S = S;
        this.k = k;
        simplices = new ArrayList<>();
        simplices.add(new Simplex(new int[]{}, 0));
        dim = -1;
        for(int i = 0; i < S.size(); i++) {
            generate(new Simplex(new int[]{i}, 0), i, new AffineHull(S, i).ball());
        }
        Collections.sort(simplices, new SimplexComparator());
        sigma = new Integer[simplices.size()];
        tau = new int[sigma.length];
        for(int i = 0; i < sigma.length; i++) {
            sigma[i] = i;
        }

        Arrays.sort(sigma, new WeightComparator());
        for(int i = 0; i < tau.length; i++) {
            tau[sigma[i]] = i;
        }

        System.out.println(simplices);
    }

    public class WeightComparator implements Comparator<Integer> {

        public int compare(Integer i, Integer j) {
            double x = simplices.get(i).getWeight() - simplices.get(j).getWeight();
            return (int) (Math.signum(x) * Math.ceil(Math.abs(x)));
        }
    }

    private void generate(Simplex s, int i, Ball b) {
        simplices.add(s);
        if(s.dim() > dim) dim = s.dim();
        if(s.dim() == k) {
            return;
        }
        for(int j = 0; j < i; j++) {
            int[] sigma = new int[s.vertices.length + 1];
            System.arraycopy(s.vertices, 0, sigma, 1, s.vertices.length);
            sigma[0] = j;
            if(b.contains(j)) {
                System.out.println("Is inside");
                generate(new Simplex(sigma, b.radius()), j, b);
            }else {
                System.out.println(Arrays.toString(S.get(j)) + " is not inside " + b);
                int[] I = new int[s.vertices.length];
                System.arraycopy(s.vertices, 0, I, 0, s.vertices.length);
                Ball mb = Miniball.welzl(S, I, I.length, new AffineHull(S, j));
                generate(new Simplex(sigma, mb.radius()), j, mb);
            }
        }
    }

    public int size() {
        return simplices.size();
    }

    public int dimension() {
        return dim;
    }

    public Simplex get(int i) {
        return simplices.get(sigma[i]);
    }

    /**Returns the faces of a simplex given through its index in compatible ordering.
     * It works through searching for the simplex in the simplices array through binary search.
     * The binary search takes log(N) * O(k), where k is the dimension of the simplex and N = sum_{i = 1}^{k}\binom{n}{i}.
     * We can actually make N smaller by determining the range that the (k-1)-simplices lie in.
     * Then N would be \binom{n}{k}.
     *
     * @param i index of simplex in compatible ordering.
     * @return faces of simplex.
     */
    public int[] faces(int i) {
        Simplex s = simplices.get(sigma[i]);
        ArrayList<Integer> indices = new ArrayList<>();
        FaceComparator c;
        for(int j = 0; j < s.vertices.length; j++) {
            c = new FaceComparator(j);
            int k = binarySearch(sigma[i], s, c, simplices);
            if(k < sigma[i] && c.compare(s, simplices.get(k)) == 0) {
                indices.add(tau[k]);
            }
        }
        return indices.stream().mapToInt(Integer::valueOf).toArray();
    }

    /**Binary searches for s without its j-th element in the ArrayList.
     *
     * @param maximum maximal index to search to.
     * @param s coface
     * @param c arbitrary comparator
     * @param simplices list to search in. It needs to be sorted with the standard ordering.
     * @return
     */
    public int binarySearch(int maximum, Simplex s, Comparator<Simplex> c, ArrayList<Simplex> simplices) {
        if(maximum == 0 || c.compare(s, simplices.get(maximum - 1)) > 0) return maximum;
        int min = 0;
        int max = maximum;
        while(min < max) {
            int mid = (min + max)/2;
            if(c.compare(s, simplices.get(mid)) > 0) min = mid + 1;
            else if(c.compare(s, simplices.get(mid)) < 0) max = mid;
            else return mid;
        }
        return min;
    }

    public class FaceComparator implements Comparator<Simplex> {

        private int j;

        public FaceComparator(int j) {
            this.j = j;
        }

        // Initialize comparator which compares the given simplices in the following way:
        // Let f(s,j) denote the face of s that is missing the j-th vertex of s in a fixed ordering(that is given here)
        // we then compare s and t through comparing f(s,j) and t. The order of the arguments is important here.

        public int compare(Simplex s, Simplex t) {
            if(s.dim() - 1 != t.dim()) return s.dim() - 1 - t.dim();
            for(int i = 0; i < s.dim(); i++) {
                if(s.vertices[i + (i >= j ? 1 : 0)] != t.vertices[i]) {
                    return s.vertices[i + (i >= j ? 1 : 0)] - t.vertices[i];
                }
            }
            return 0;
        }
    }
}
