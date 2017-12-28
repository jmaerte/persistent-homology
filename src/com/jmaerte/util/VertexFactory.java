package com.jmaerte.util;

import com.jmaerte.lin_alg.Vector;

import java.util.HashMap;

/**
 * Created by Julian on 24/12/2017.
 */
public class VertexFactory<V extends Vector<V>> {

    HashMap<V, Integer> idMap;
    HashMap<Integer, V> vectorMap;
    int currID = 0;

    public VertexFactory() {
        idMap = new HashMap<>();
        vectorMap = new HashMap<>();
    }

    public void create(V vertex) {
        idMap.put(vertex, currID);
        vectorMap.put(currID, vertex);
        currID++;
    }

    public V getVertex(int id) {
        return vectorMap.get(id);
    }

    public int getID(V vertex) {
        return idMap.get(vertex);
    }
}
