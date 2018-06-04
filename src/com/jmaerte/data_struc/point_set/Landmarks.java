package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.calc.Function;
import com.jmaerte.util.calc.Util;
import com.jmaerte.util.input.Writable;
import com.jmaerte.util.log.Logger;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**This class represents a subset of landmarks of a PointSet S, i.e. int[] l \subset S,
 * equipped with an matrix D of Size l.length x (s.size() - l.length) where D[i,j] = d(l[i], S.get(j)).
 *
 */
public class Landmarks<T extends Writable & Function<T, Double>> extends PointSet<T> {

    private PointSet<T> S;
    private double[][] D;
    private int[] landmarks;
    private int[] order;
    private int n, N;

    public Landmarks(PointSet<T> S, int[] landmarks, double[][] D) {
        this.S = S;
        this.landmarks = landmarks;
        this.D = D;
    }

    public Landmarks(PointSet<T> S, int n, boolean isMaxmin) {
        this(S, n, isMaxmin ? Choice.MAXMIN : Choice.RANDOM);
    }

    public Landmarks(PointSet<T> S, int n, Choice c) {
        this.n = n;
        this.N = S.size() - n;
        int[] landmarks = new int[n];
        double[][] D = new double[n][S.size()];
        if(c == Choice.MAXMIN) {
            int next = ThreadLocalRandom.current().nextInt(0, S.size());
            double[] min = new double[S.size()];
            order = new int[landmarks.length];
            Logger.progress(n, "Landmarks");
            for(int i = 0; i < n; i++) {
                int p = Util.binarySearch(next, landmarks, 0, i);
                if(i - p > 0) {
                    System.arraycopy(landmarks, p, landmarks, p + 1, i - p);
                    System.arraycopy(order, p, order, p + 1, i - p);
                }
                landmarks[p] = next;
                order[p] = i;

                int l = 0;
                int maxIndex = -1;
                double max = -1;
                for(int k = 0; k < S.size(); k++) {
                    if(l <= i && k == landmarks[l]) l++;
                    else {
                        D[order[p]][k] = S.d(landmarks[p], k);
                        if(i == 0 || D[order[p]][k] < min[k]) min[k] = D[order[p]][k];
                        if(min[k] > max) {
                            max = min[k];
                            maxIndex = k;
                        }
                    }
                }
                next = maxIndex;
                Logger.updateProgress(i);
            }
            Logger.close();
        }else {
            for(int i = 0; i < n; i++) {
                int j = ThreadLocalRandom.current().nextInt(0, S.size());
                int k = Util.binarySearch(j, landmarks, 0, i);
                if(k < i && landmarks[k] == j) i--;
                else {
                    if(i - k > 0) System.arraycopy(landmarks, k, landmarks, k + 1, i - k);
                    landmarks[k] = j;
                }
            }
            for(int i = 0; i < n; i++) {
                int l = 0;
                for(int j = 0; j < S.size(); j++) {
                    if(l < n && j == landmarks[l]) l++;
                    else {
                        D[i][j - l] = S.d(i, j);
                    }
                }
            }
        }
        this.S = S;
        this.landmarks = landmarks;
        this.D = D;
    }

    public double getValuation(int i, int j) {
        int l = 0;
        double maxmin = 0;
        for(int k = 0; k < S.size(); k++) {
            if(l < n && landmarks[l] == k) l++;
            else {
                double curr = Math.max(D[order[i]][k], D[order[j]][k]);
                if(k == 0 || curr < maxmin) maxmin = curr;
            }
        }
        return maxmin;
    }

    public static enum Choice {
        MAXMIN, RANDOM;
    }

    public int size() {
        return n;
    }

    public String toString() {
        return Arrays.toString(landmarks);
    }

    public T get(int i) {
        return S.get(landmarks[i]);
    }

    public double d(Integer i, Integer j) {
        return S.d(landmarks[i], landmarks[j]);
    }

    public double d(T x, T y) {
        return x.eval(y);
    }

    public PointSet<T> pointSet() {
        return S;
    }

    public int[] landmarks() {
        return landmarks;
    }
}