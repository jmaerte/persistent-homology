package com.jmaerte.data_struc.point_set;

import com.jmaerte.util.calc.Util;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**This class represents a subset of landmarks of a PointSet S, i.e. int[] l \subset S,
 * equipped with an matrix D of Size l.length x (s.size() - l.length) where D[i,j] = d(l[i], S.get(j)).
 *
 */
public class Landmarks {

    private PointSet S;
    private double[][] D;
    private int[] landmarks;
    private int n, N;

    public Landmarks(PointSet S, int[] landmarks, double[][] D) {
        this.S = S;
        this.landmarks = landmarks;
        this.D = D;
    }

    public Landmarks(PointSet S, int n, Choice c) {
        this.n = n;
        this.N = S.size() - n;
        int[] landmarks = new int[n];
        double[][] D = new double[n][N];
        if(c == Choice.MINMAX) {
            int next = ThreadLocalRandom.current().nextInt(0, S.size());
            double[][] curr = new double[n][S.size()];
            double[] min = new double[S.size()];
            for(int i = 0; i < n; i++) {
                int p = Util.binarySearch(next, landmarks, 0, i);
                if(i - p > 0) {
                    System.arraycopy(landmarks, p, landmarks, p + 1, i - p);
                    System.arraycopy(curr, p, curr, p + 1, i - p);
                }
                landmarks[p] = next;

                if(i + 1 != n) {
                    int l = 0;
                    int maxIndex = -1;
                    double max = -1;
                    for(int k = 0; k < S.size(); k++) {
                        if(l <= i && k == landmarks[l]) l++;
                        else {
                            curr[p][k] = S.d(landmarks[p], k);
                            if(i == 0 || curr[p][k] < min[k]) min[k] = curr[p][k];
                            if(min[k] > max) {
                                max = min[k];
                                maxIndex = k;
                            }
                        }
                    }
                    next = maxIndex;
                }
            }
            int l = 0;
            for(int j = 0; j < S.size(); j++) {
                if(l < n && landmarks[l] == j) l++;
                else {
                    double m = 0;
                    for(int i = 0; i < n; i++) {
                        if(i == 0 || curr[i][j] < m) m = curr[i][j];
                        D[i][j-l] = curr[i][j];
                    }
                }
            }
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
            System.out.println(Arrays.toString(landmarks));
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
        double minmax = 0;
        for(int k = 0; k < N; k++) {
            double curr = Math.max(D[i][k], D[j][k]);
            if(k == 0) minmax = curr;
            else if(curr < minmax) minmax = curr;
        }
        return minmax;
    }

    public static enum Choice {
        MINMAX, RANDOM;
    }

    public String toPlot() {
        String p = PointSetUtils.toFilePlot(S);
        String landmarks = "c(";
        for(int i = 0; i < n; i++) {
            landmarks += (this.landmarks[i] + 1) + (i + 1 == n ? ")" : ", ");
        }
        return p + "\n" +
                "landmarks <- " + landmarks + "\n" +
                "plot <- plot + geom_point(data = subset(data, id %in% landmarks), aes(x = x0, y = x1), colour=\"firebrick1\", size=1.5, shape=2, fill = 1)\n" +
                "print(plot)";
    }
}