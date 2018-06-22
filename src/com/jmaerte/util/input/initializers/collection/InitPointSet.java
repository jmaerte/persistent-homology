package com.jmaerte.util.input.initializers.collection;

import com.jmaerte.data_struc.point_set.PointSetUtils;
import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.initializers.Initializer;
import com.jmaerte.util.vector.Vector2D;

public class InitPointSet extends Initializer {

    public Vector2D<String, Object> init(String[] params) {
        if(params[0].equals("mapping")) {
            switch(params[1]) {
                case "Torus":
                    int n = Integer.valueOf(params[2]);
                    int r = (params.length < 5) ? 5 : Integer.valueOf(params[3]);
                    int R = (params.length < 5) ? 10 : Integer.valueOf(params[4]);
                    return new Vector2D<>("A " + n + " sized sample of the Torus", PointSetUtils.getFromMapping(n, new double[]{0, 2*Math.PI, 0, 2 * Math.PI},
                    PointSetUtils.torusChart(5, 10)));
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
