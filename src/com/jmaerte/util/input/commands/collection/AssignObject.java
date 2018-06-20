package com.jmaerte.util.input.commands.collection;

import com.jmaerte.util.input.Modifier;
import com.jmaerte.util.input.Register;
import com.jmaerte.util.input.commands.Command;
import com.jmaerte.util.input.initializers.Initializers;

public class AssignObject extends Command {

    public void call(String[] params) {
        String[] initP = new String[params.length - 1];
        System.arraycopy(params, 1, initP, 0, params.length - 1);
        try {
            Register.push(params[0], Initializers.init(initP));
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    protected String description() {
        return "Assigns [object] to [var]. The [object] has to be an Object initializer. If you need advice on how to initialize Objects " +
                "type \"help init\"";
    }

    protected String[] params() {
        return new String[]{"var", "object"};
    }

    protected String[] options() {
        return new String[]{};
    }

    protected Modifier[] modifiers() {
        return new Modifier[]{};
    }

    protected String command() {
        return "<-";
    }

    protected int positionCommand() {
        return 1;
    }
}
