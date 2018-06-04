package com.jmaerte.util.input.commands.collection;

import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.commands.Command;

public class ListObjects extends Command {

    protected void call(String[] params) {
        System.out.println(Register.print());
    }

    protected String description() {
        return "List all user defined variables.";
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
        return "lo";
    }

    protected int positionCommand() {
        return 0;
    }

}
