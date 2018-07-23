package com.jmaerte.util.input.commands.collection;

import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.commands.Command;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class SetHome extends Command {

    public String call(String[] params) {
        // TODO Set the home directory.
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(System.getProperty("user.home") + File.separator + ".persistent_homology" + File.separator + "config"), false));
            bw.write(params[0]);
            bw.flush();
            bw.close();
        }catch(Exception e) {
            System.err.println("Couldn't load the config file.");
            return "Couldn't load the config file.";
        }
        System.out.println("Successfully set home directory. I keep logging to the file that I created in the old home directory as said at the program start.");
        return "Home directory is now set to " + params[0] + ". I will keep logging to this file.";
    }

    protected String description() {
        return "Set the home directory. This is the directory where the logs and writable objects are saved.";
    }

    protected String[] params() {
        return new String[]{"path"};
    }

    protected String[] options() {
        return new String[]{};
    }

    protected Modifier[] modifiers() {
        return new Modifier[]{};
    }

    protected String command() {
        return "home";
    }

    protected int positionCommand() {
        return 0;
    }
}
