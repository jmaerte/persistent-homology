package com.jmaerte.util.input.commands;

public abstract class Command {

    protected abstract void call(String[] params);
    protected abstract String[] params();
    protected abstract String[] options();
    protected abstract String[] modifiers();
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
        String[] m = modifiers();
        String s = "";
        for(int i = 0; i < m.length; i++) {
            s += "--" + m[i] + (i + 1 != m.length ? ", " : "");
        }
        return s;
    }
}
