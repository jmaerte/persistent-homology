package com.jmaerte.util.input.initializers;

import com.jmaerte.util.input.Table;
import com.jmaerte.util.input.initializers.collection.InitFiltration;
import com.jmaerte.util.input.initializers.collection.InitLandmarks;
import com.jmaerte.util.input.initializers.collection.InitPersistence;
import com.jmaerte.util.input.initializers.collection.InitPointSet;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector3D;

import java.util.HashMap;
import java.util.Map;

public class Initializers {

    private static final HashMap<String, Initializer> initializer = new HashMap<>();

    static {
        initializer.put("Filtration", new InitFiltration());
        initializer.put("LandmarkSet", new InitLandmarks());
        initializer.put("Persistence", new InitPersistence());
        initializer.put("PointSet", new InitPointSet());
    }

    public static Vector3D<String, Object, Class> init(String[] params) throws Exception {
        String command = params[0];
        String[] newParams = new String[params.length - 1];
        System.arraycopy(params, 1, newParams, 0, newParams.length);
        for(Map.Entry<String, Initializer> e : initializer.entrySet()) {
            if(e.getKey().equals(command)) return e.getValue().init(newParams);
        }
        throw new Exception("Can't find command " + params[0] + "!");
    }

    public static void print() {
        Table t = new Table(new int[]{25, 10, 10, 40}, new String[]{"Syntax", "Options", "Modifiers", "Description"});
        for(Initializer in : initializer.values()) {
            t.addEntry(new String[]{in.syntax(), in.option(), in.modifier(), in.description()});
            t.addEmpty();
        }
        t.addBorder();
        System.out.println(t);
    }

}
