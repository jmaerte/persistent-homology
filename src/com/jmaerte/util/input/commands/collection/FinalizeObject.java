package com.jmaerte.util.input.commands.collection;

import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.commands.Command;

public class FinalizeObject extends Command {

    public String call(String[] params) {
        Register.finalize(params[0]);
        return "Finalized " + params[0];
    }

    protected String description() {
        return "Deletes certain Variable and its associated Object from memory.";
    }

    protected String[] params() {
        return new String[]{"var"};
    }

    protected String[] options() {
        return new String[]{};
    }

    protected Modifier[] modifiers() {
        return new Modifier[]{};
    }

    protected String command() {
        return "finalize";
    }

    protected int positionCommand() {
        return 0;
    }
}
