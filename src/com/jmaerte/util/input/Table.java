package com.jmaerte.util.input;

public class Table {

    private String s = "";
    private int[] columnWidths;

    public Table(int[] columnWidths, String[] headers) {
        this.columnWidths = columnWidths;
        addBorder();
        addEntry(headers);
        addBorder();
    }

    public void addBorder() {
        for(int i = 0; i < columnWidths.length; i++) {
            s += "+";
            for(int j = 0; j < columnWidths[i] + 2; j++) {
                s += "-";
            }
        }
        s+="+\n";
    }

    public void addEntry(String[] entry) {
        boolean running = true;
        while(running) {
            s += "| ";
            boolean turn = false;
            for(int i = 0; i < columnWidths.length; i++) {
                int lastSpace = -1;
                String curr = "";
                if(entry[i].length() > columnWidths[i]) {
                    String token = "";
                    for(int l = 0; l < columnWidths[i]; l++) {
                        char c = entry[i].charAt(l);
                        if(c == ' ') {
                            lastSpace = l;
                            curr += token + " ";
                            token = "";
                        }else {
                            token += c;
                        }
                    }
                    if(lastSpace < 0) {
                        curr = token;
                    }
                    entry[i] = entry[i].substring(curr.length(), entry[i].length());
                }else {
                    curr = entry[i];
                    entry[i] = "";
                }
                if(i != 0) s += " ";
                s += curr;
                for(int k = 0; k < columnWidths[i] - curr.length(); k++) {
                    s += " ";
                }
                s += " |";
                if(entry[i].length() > 0) turn = true;
            }
            s += "\n";
            running = turn;
        }
    }

    public void addEmpty() {
        String[] em = new String[columnWidths.length];
        for(int i = 0; i < em.length; i++) {
            em[i] = "";
        }
        addEntry(em);
    }

    public String toString() {
        return s;
    }

}
