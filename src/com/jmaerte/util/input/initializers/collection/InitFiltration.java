package com.jmaerte.util.input.initializers.collection;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.initializers.Initializer;
import com.jmaerte.util.vector.Vector2D;

public class InitFiltration extends Initializer {

    public Vector2D<String, Object> init(String[] params) {
        Vector2D<Object, Class> v = Register.get(params[2]);
        int k = Integer.valueOf(params[0]);
        PointSet S = null;
        Landmarks L = null;
        try {
            if(params[1].equals("witness")) {
                L = (Landmarks) v.getFirst();
            }else {
                S = (PointSet) v.getFirst();
            }
        }catch(Exception e) {
            System.out.println(params[1] + " is not fitting here.");
        }
        switch(params[1]) {
            case "cech":
                return new Vector2D<>(k + "-Skeleton of Čech(" + params[2] + ")", Filtration.cech(S, k));
            case "vietoris":
                return new Vector2D<>(k + "-Skeleton of Viet(" + params[2] + ")", Filtration.vietoris(S, k));
            case "witness":
                return new Vector2D<>(k + "-Skeleton of Witness(" + params[2] + ")", Filtration.witness_lazy(L, k));
        }
        return new Vector2D<>("", null);
    }

    public String description() {
        return "Initializes the [k]-Skeleton of the filtration of type [type], which is either one of \"cech\", \"vietoris\" or \"witness\", " +
                "with given PointSet [S] in case of cech and vietoris and given LandmarkSet [S] in case of witness.";
    }

    public String[] params() {
        return new String[]{"k", "type", "S"};
    }

    public String[] options() {
        return new String[]{};
    }

    public Modifier[] modifiers() {
        return new Modifier[]{};
    }

    public String command() {
        return "Filtration";
    }


}
