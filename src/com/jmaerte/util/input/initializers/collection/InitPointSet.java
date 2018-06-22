package com.jmaerte.util.input.initializers.collection;

import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.data_struc.point_set.PointSetUtils;
import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.initializers.Initializer;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector3D;

public class InitPointSet extends Initializer {

    public Vector3D<String, Object, Class> init(String[] params) {
        if(params[0].equals("mapping")) {
            switch(params[1]) {
                case "Torus":
                    int n = Integer.valueOf(params[2]);
                    int r = (params.length < 5) ? 5 : Integer.valueOf(params[3]);
                    int R = (params.length < 5) ? 10 : Integer.valueOf(params[4]);
                    return new Vector3D<>("A " + n + " points sized sample of the Torus with parameters r=" + r + " and R=" + R + ".", PointSetUtils.getFromMapping(n, new double[]{0, 2*Math.PI, 0, 2 * Math.PI},
                    PointSetUtils.torusChart(5, 10)), PointSet.class);
            }
        }
        return null;
    }

    public String description() {
        return "Initialize a PointSet. For now this is only working for mapping samples.";
    }

    public String[] params() {
        return new String[]{"type"};
    }

    public String[] options() {
        return new String[]{};
    }

    public Modifier[] modifiers() {
        return new Modifier[]{};
    }

    public String command() {
        return "PointSet";
    }
}
