package com.jmaerte.simplicial;

import com.jmaerte.lin_alg.RealVector;
import com.jmaerte.lin_alg.Vector;
import com.jmaerte.util.Ball;
import com.jmaerte.util.Metric;
import com.jmaerte.util.VertexFactory;

import java.util.ArrayList;

import static com.jmaerte.lin_alg.Standard.euclidean;

/**
 * Created by Julian on 23/12/2017.
 */
public class Simplex<V extends Vector<V>> {

    int[] vertexIDs;
    VertexFactory<V> factory;
    Metric<V> d;
    double radius;

    /**Creates a Simplex from a given id array. This array needs to be sorted for comparison purposes.
     *
     * @param vertexIDs
     * @param factory
     */
    public Simplex(int[] vertexIDs, VertexFactory<V> factory, Metric<V> d) {
        this.vertexIDs = vertexIDs;
        this.factory = factory;
        this.d = d;
        // TODO: Calculate radius field as the radius of the SEB of this simplex.
        int[] interior = new int[vertexIDs.length];
        for(int i = 0; i < interior.length; i++) {
            interior[i] = i;
        }
        this.radius = seb(interior.length, interior, new int[vertexIDs.length]).radius;
    }

    private Ball<V> seb(int n, ArrayList<Integer>[] interior, ArrayList<Integer>[] boundary) {

    }

    public boolean equals(Simplex s) {
        for(int i = 0; i < vertexIDs.length; i++) {
            if(vertexIDs[i] != s.vertexIDs[i]) return false;
        }
        return true;
    }
}
