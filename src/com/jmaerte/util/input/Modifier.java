package com.jmaerte.util.input;

public enum Modifier {
    MAXMIN("maxmin"),
    CSV("csv"),
    REDUCED("reduced"),
    BALLS("balls");

    public final String mod;

    Modifier(final String mod) {
        this.mod = mod;
    }

    public String toString() {
        return mod;
    }
}
