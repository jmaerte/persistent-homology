package com.jmaerte.util.input.commands.collection;

import com.jmaerte.util.input.commands.Command;
import com.jmaerte.util.input.commands.Commands;
import com.jmaerte.util.input.initializers.Initializers;

public class Help extends Command {

    public void call(String[] params) {
        if(params.length == 0) {
            Commands.print();
        }else if(params[0].equals("init")){
            Initializers.print();
        }
    }

    protected String description() {
        return "Shows a help overview like this. type can be either none or \"init\", where none shows you this table and \"init\" shows you the" +
                " help table for initializing objects. (so you can use the following command)";
    }

    protected String[] params() {
        return new String[]{"type"};
    }

    protected String[] options() {
        return new String[]{};
    }

    protected String[] modifiers() {
        return new String[]{};
    }

    protected String command() {
        return "help";
    }

    protected int positionCommand() {
        return 0;
    }
}
