package com.jmaerte.util.input.commands.collection;

import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.commands.Command;

public class Clear extends Command {

    public String call(String[] params) {
        Register.free();
        return "Cleared the memory space.";
    }

    protected String description() {
        return "Free user allocated memory.";
    }

    protected String[] params() {
        return new String[]{};
    }

    protected String[] options() {
        return new String[]{};
    }

    protected Modifier[] modifiers() {
        return new Modifier[]{};
    }

    protected String command() {
        return "clear";
    }

    protected int positionCommand() {
        return 0;
    }
}
