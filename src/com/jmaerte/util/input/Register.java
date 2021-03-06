package com.jmaerte.util.input;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.Metadata;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.persistence.Persistence;
import com.jmaerte.util.vector.Vector2D;
import com.jmaerte.util.vector.Vector3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class Register {

    private static HashMap<String, Object> objects = new HashMap<>();
    private static HashMap<String, Class> classes = new HashMap<>();
    private static HashMap<String, String> descriptions = new HashMap<>();

    public static final HashMap<Class, String> typeNames;
//    public static final HashMap<String, Metadata> pointSetTypes;

    static {
        // Give every object class a output name
        typeNames = new HashMap<>();
        typeNames.put(Filtration.class, "Filtration");
        typeNames.put(PointSet.class, "PointSet");
        typeNames.put(Persistence.class, "PersistenceDiagram");
        typeNames.put(Landmarks.class, "Landmarks-/Point Set");

        // Init PointSetTypes, i.e. Euclidean, Lexicographic and other formats of metric data
//        pointSetTypes = new HashMap<>();
    }

    public static void push(String name, String description, Object o) {
        objects.put(name, o);
        descriptions.put(name, description);
        classes.put(name, o.getClass());
    }

    public static void push(String name, String description, Object o, Class c) {
        objects.put(name, o);
        descriptions.put(name, description);
        classes.put(name, c);
    }

    public static void push(String name, Vector2D<String, Object> v) {
        objects.put(name, v.getSecond());
        descriptions.put(name, v.getFirst());
        classes.put(name, v.getSecond().getClass());
    }

    public static String push(String name, Vector3D<String, Object, Class> v) {
        push(name, v.getFirst(), v.getSecond(), v.getThird());
        return "Successfully initialized the variable " + name + " of type " + typeNames.get(v.getThird()) + ".";
    }

    public static Vector2D<Object, Class> get(String name) throws NoSuchElementException {
        Object o = objects.get(name);
        if(o == null) throw new NoSuchElementException("Unknown Object: " + name);
        return new Vector2D<>(objects.get(name), classes.get(name));
    }

    public static String describe(String name) {
        String o = descriptions.get(name);
        if(o == null) throw new NoSuchElementException("Unknown Object: " + name);
        return o;
    }

    public static String print() {
        String s = "User initialized Objects in Memory = [\n";
        for(Map.Entry<String, Object> e : objects.entrySet()) {
            String name = e.getKey();
            s += "\t{" + name + " - Type: " + typeNames.get(classes.get(name)) + " - Description: " + descriptions.get(name) + "}\n";
        }
        return s + "]";
    }

    public static void finalize(String name) {
        objects.remove(name);
        classes.remove(name);
        descriptions.remove(name);
    }

    public static void free() {
        objects = new HashMap<>();
        classes = new HashMap<>();
        descriptions = new HashMap<>();
    }
}
