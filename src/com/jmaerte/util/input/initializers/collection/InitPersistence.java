package com.jmaerte.util.input.initializers.collection;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.persistence.Persistence;
import com.jmaerte.util.input.Input;
import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.initializers.Initializer;
import com.jmaerte.util.vector.Vector2D;

public class InitPersistence extends Initializer {

    public Vector2D<String, Object> init(String[] params) {
        Vector2D<Object, Class> v = Register.get(params[0]);
        Filtration f = null;
        boolean reduced = Input.is(Modifier.REDUCED);
        try {
            f = (Filtration) v.getFirst();
        }catch(Exception e) {
            System.out.println(params[1] + " is not of type Filtration.");
        }
        return new Vector2D<>("The " + (reduced ? "reduced" : "ordinary") + " Persistent Homology diagrams of " + params[0] + ".", new Persistence(f, reduced));
    }

    public String description() {
        return "Calculates Persistent Homology of a Filtration [f] and saves it into a user-specified variable so he can draw the diagrams. ";
    }

    public String[] params() {
        return new String[]{"f"};
    }

    public String[] options() {
        return new String[]{};
    }

    public Modifier[] modifiers() {
        return new Modifier[]{Modifier.REDUCED};
    }

    public String command() {
        return "Persistence";
    }
}
