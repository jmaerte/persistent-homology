package com.jmaerte.data_struc.point_set;

import java.util.HashMap;

public interface VertexFactory extends Metric<Integer> {

    int size();
    double d(Integer i, Integer j);

}
