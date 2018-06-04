package com.jmaerte.util.input.commands;

import com.jmaerte.util.input.Modifier;

public abstract class Command {

    /**Defines a call. Give all needed parameters to this method.
     *
     * @param params parameters.
     */
    protected abstract void call(String[] params);
    protected abstract String[] params();
    protected abstract String[] options();
    protected abstract Modifier[] modifiers();
    protected abstract String command();
    protected abstract String description();
    protected abstract int positionCommand();

    public String syntax() {
        String[] p = params();
        String s = "";
        boolean added = false;
        for(int i = 0; i < p.length; i++) {
            if(i == positionCommand() && !added) {
                s += command() + (i + 1 <= p.length ? " " : "");
                added = true;
                i--;
            }else {
                s += "[" + p[i] + "]" + (i + 1 != p.length || !added ? " " : "");
            }
        }
        if(!added) s += command();
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
