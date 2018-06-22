package com.jmaerte.util.input.initializers.collection;

import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.input.Input;
import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.initializers.Initializer;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector3D;

public class InitLandmarks extends Initializer {

    public Vector3D<String, Object, Class> init(String[] params) {
        Vector2D<Object, Class> v = Register.get(params[1]);
        int n = Integer.valueOf(params[0]);
        PointSet S = null;
        try {
            S = (PointSet) v.getFirst();
        }catch(Exception e) {
            System.out.println(params[1] + " is not fitting here.");
        }
        return new Vector3D<>("A" + (Input.is(Modifier.MAXMIN) ? " by maxmin " : " randomly ") + "chosen LandmarkSet of the PointSet " + params[1],
                new Landmarks(S, n, Input.is(Modifier.MAXMIN)), Landmarks.class);
    }

    public String description() {
        return "Get a LandmarkSet of magnitude [n] from a given PointSet [S], that is either chosen by maxmin or randomly. You can specifiy the " +
                "algorithm used by setting the modifier --maxmin.";
    }

    public String[] params() {
        return new String[]{"n", "S"};
    }

    public String[] options() {
        return new String[]{};
    }

    public Modifier[] modifiers() {
        return new Modifier[]{Modifier.MAXMIN};
    }

    public String command() {
        return "LandmarkSet";
    }

}
