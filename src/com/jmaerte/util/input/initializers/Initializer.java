package com.jmaerte.util.input.initializers;

import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.vector.Vector2D;

public abstract class Initializer {

    public abstract Vector2D<String, Object> init(String[] params);
    public abstract String[] params();
    public abstract String[] options();
    public abstract Modifier[] modifiers();
    public abstract String command();
    public abstract String description();

    public String syntax() {
        String[] p = params();
        String s = command();
        for(String st : p) {
            s += " [" + st + "]";
        }
        return s;
    }

    public String option() {
        String[] m = options();
        String s = "";
        for(int i = 0; i < m.length; i++) {
            s += "-" + m[i] + (i + 1 != m.length ? ", " : "");
        }
        return s;
    }

    public String modifier() {
        Modifier[] m = modifiers();
        String s = "";
        for(int i = 0; i < m.length; i++) {
            s += "--" + m[i] + (i + 1 != m.length ? ", " : "");
        }
        return s;
    }

}
