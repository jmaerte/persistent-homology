package com.jmaerte.data_struc.complex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Simplex {

    protected int[] vertices;
    private double weight;

    public Simplex(int[] values, double weight) {
        this.vertices = values;
        this.weight = weight;
    }

    public int dim() {
        return vertices.length - 1;
    }

    public int get(int i) {
        return vertices[i];
    }

    public String toString() {
        return Arrays.toString(vertices) + " - " + weight;
    }

    public double getWeight() {
        return weight;
    }

    public int[] getVertices() {
        return vertices;
    }

//    private final VertexFactory factory;
//
//    // stores codimension 1 faces of this instance.
//    private Simplex[] faces;
//    private int fill;
//
//    // Map that saves the valuations of this simplex in different filtration f.e. Cech or Vietoris filtrations.
//    private Map<Integer, Double> valuations;
//
//    /**Initialize maximal simplex of a given factory, i.e. for a given set of vertices indexed by [n]u{0}
//     *
//     * @param factory the factory to take vertices from
//     */
//    public Simplex(VertexFactory factory) {
//        this.factory = factory;
//        this.faces = new Simplex[factory.size()];
//        this.vertices = new int[factory.size()];
//        this.valuations = new HashMap<>();
//        for(int i = 0; i < vertices.length; i++) {
//            vertices[i] = i;
//        }
//    }
//
//    /**Initializes the codimension 1 face of a given simplex.
//     *
//     * @param parent Simplex to get the codimension 1 face of.
//     * @param i the index of the vertex to remove.
//     */
//    public Simplex(Simplex parent, int i) {
//        this.factory = parent.factory;
//        this.faces = new Simplex[parent.dim()];
//        this.vertices = new int[parent.dim()];
//        this.valuations = new HashMap<>();
//        System.arraycopy(parent.vertices, 0, vertices, 0, i);
//        System.arraycopy(parent.vertices, i + 1, vertices, i, parent.dim() - i);
//    }
//
//    public int dim() {
//        return vertices.length - 1;
//    }
//
//    public void addFace(Simplex face) {
////        info("added " + face.toString() + " to " + this + " in " + fill);
//        faces[fill++] = face;
//    }
//
//    /**Sets the valuation for a certain filtration. The filtration to set value for is referenced by an identifier.
//     * F.e. you can use the VIETORIS-identfier for referencing to the value in a Vietoris-filtration
//     *
//     * @param identifier
//     * @param val
//     */
//    public void setValuation(int identifier, double val) {
//        valuations.put(identifier, val);
//    }
//
//    public double getValuation(int identifier) {
//        return valuations.get(identifier);
//    }
//
//    public int get(int index) {
//        return vertices[index];
//    }
//
//    public String toString() {
//        String face = "[";
//        for(int i = 0; i < faces.length; i++) {
//            face += (faces[i] != null ? Arrays.toString(faces[i].vertices) : "null") + (i + 1 == faces.length ? "" : ", ");
//        }
//        return Arrays.toString(vertices) + " -> " + getValuation(0);
//    }
//
//    public Simplex getFace(int i) {
//        assert i < faces.length;
//        return faces[i];
//    }
}
class SimplexComparator implements Comparator<Simplex> {

    public int compare(Simplex s, Simplex t) {
        if(s.dim() != t.dim()) return s.dim() - t.dim();
        for(int i = 0; i <= s.dim(); i++) {
            if(s.vertices[i] != t.vertices[i]) return s.vertices[i] - t.vertices[i];
        }
        return 0;
    }
}