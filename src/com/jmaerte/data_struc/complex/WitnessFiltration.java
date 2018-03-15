package com.jmaerte.data_struc.complex;

import com.jmaerte.data_struc.point_set.VertexFactory;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class WitnessFiltration {

    private int[] landmarks;
    private int[] data;
    // distances[i][j] is the distance from the i-th data-point to the j-th landmark.
    private double[][] distances;

    /**Creates a new instance of a witness complex with minmax pivotization.
     *
     * @param factory The Point-Set Data.
     * @param n the amount of landmarks to create.
     */
    public WitnessFiltration(VertexFactory factory, int n) {
        landmarks = new int[n];
        landmarks[0] = ThreadLocalRandom.current().nextInt(0, factory.size());
        distances = new double[factory.size() - n][n];
        data = new int[factory.size() - n];

        int[] currData = new int[factory.size()];
        for(int i = 0; i < currData.length; i++) {
            currData[i] = i;
        }
        System.arraycopy(currData, landmarks[0] + 1, currData, landmarks[0], factory.size() - landmarks[0] - 1);
        System.out.println(landmarks[0]);
        double[][] currDistances = new double[factory.size()][n];

        double[] min = new double[factory.size()];

        for(int j = 0; j < factory.size(); j++) {
            currDistances[currData[j]][0] = factory.d(currData[j], landmarks[0]);
            min[currData[j]] = currDistances[currData[j]][0];
        }

        for(int i = 1; i < n; i++) {
            // Calculate the maximizer of the minimum distance function.
            double max = -1;
            int point = -1;
            for(int j = 0; j < factory.size() - i; j++) {
                if(min[currData[j]] > max) {
                    max = min[currData[j]];
                    point = j;
                }
            }
            // set the maximizer to be the next landmark
            landmarks[i] = currData[point];
            if(factory.size() - point > 0) {
                System.arraycopy(currData, point + 1, currData, point, factory.size() - point - 1);
            }
            for(int j = 0; j < factory.size() - i - 1; j++) {
                currDistances[currData[j]][i] = factory.d(currData[j], landmarks[i]);
                if(min[currData[j]] > currDistances[currData[j]][i]) {
                    min[currData[j]] = currDistances[currData[j]][i];
                }
            }
        }

        for(int i = 0; i < factory.size() - n; i++) {
            data[i] = currData[i];
            distances[i] = currDistances[currData[i]];
        }

        System.out.println(Arrays.toString(landmarks));
        System.out.println(Arrays.toString(data));
        for(int i = 0; i < distances.length; i++) {
            for(int j = 0; j < distances[i].length; j++) {
                System.out.print(distances[i][j] + "\t");
            }
            System.out.print("\n");
        }
    }


    private void generate() {

    }

}
