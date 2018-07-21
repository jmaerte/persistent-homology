package com.jmaerte.util.input.commands.collection;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.input.FileIO;
import com.jmaerte.util.input.Input;
import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.commands.Command;
import com.jmaerte.util.vector.Vector2D;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class SaveObject extends Command {

    public String call(String[] params) {
        Vector2D<Object, Class> v = Register.get(params[0]);
        BufferedWriter bw = null;
        String path = null;
        if(Input.is(Modifier.HOME)) {
            path = Input.HOME + File.separator + "saves" + File.separator + params[1];
        }else {
            path = params[1];
        }
        try {
            bw = new BufferedWriter(new FileWriter(new File(path)));
            if(v.getSecond().getSimpleName().equals("PointSet") || v.getSecond().getSimpleName().equals("Landmarks")) {
                PointSet S = (PointSet) v.getFirst();
                S.write(bw);
            }else if(v.getSecond().getSimpleName().equals("Filtration")) {
                Filtration f = (Filtration) v.getFirst();
                f.write(bw);
            }
            bw.close();
        }catch(Exception e) {
            return "Fatal File-Write Error occurred. Can't write to file " + path + ".";
        }
        return "Successfully wrote " + params[0] + " to the file " + path + ".";
    }

    protected String description() {
        return "Save an object to a file. This is possible for PointSets (in particular LandmarkSets) and Filtrations." +
                "The path is either an absolute path or if you add the home-modifier a relative path to the user-home directory.";
    }

    protected String[] params() {
        return new String[]{""};
    }

    protected String[] options() {
        return new String[]{"path"};
    }

    protected Modifier[] modifiers() {
        return new Modifier[]{Modifier.HOME};
    }

    protected String command() {
        return "save";
    }

    protected int positionCommand() {
        return 0;
    }
}
