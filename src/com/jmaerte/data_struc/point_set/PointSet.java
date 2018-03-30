package com.jmaerte.data_struc.point_set;

/**
 * An instance that stores an <i>n</i> point subset of the <i>same</i> euclidean space(meaning that their dimension has
 * to be the same, what is no restriction for our purposes).
 * This provides an interface to the user since he is not bound to deliver the points in a certain data structure, what
 * will be necessary in further progress.
 *
 */
public interface PointSet extends VertexFactory, Metric<double[]> {

    /**Get the euclidean dimension of the points.
     *
     * @return Dimension of the PointSet
     */
    int dimension();

    /**Providing access to the components of the vectors.
     * The ordering is fixed.
     *
     * @param i: Index of the vector.
     * @param j: Component index.
     * @return in case the <i>i</i>th vector is (x_k)_{0 <=k < {@code dimension()}} returns x_j.
     */
    double get(int i, int j);

    /**Providing access to vector-valued data.
     *
     * @param i index of the vector to get
     * @return the vector as double[]
     */
    double[] get(int i);

    /**Providing an overview on the cardinality of the point point_set.
     *
     * @return card(S)
     */
    int size();
}
