package com.jmaerte.util.input.commands.collection;

import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.commands.Command;

public class Clear extends Command {

    public void call(String[] params) {
        Register.free();
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

    protected String[] modifiers() {
        return new String[]{};
    }

    protected String command() {
        return "clear";
    }

    protected int positionCommand() {
        return 0;
    }
}
