package com.jmaerte.util.input;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.PointSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Input {

    private static final char[] textQualifiers = new char[]{'\'', '"'};

    HashMap<String, Filtration> filtrations;
    HashMap<String, PointSet> pointsets;
    HashMap<String, Landmarks> landmarks;
    Scanner scanner;

    public Input() {
        this.filtrations = new HashMap<>();
        this.pointsets = new HashMap<>();
        this.landmarks = new HashMap<>();
        scanner = new Scanner(System.in);
    }

    public void input() {
        process(scanner.next());
        input();
    }

    private void process(String command) {
        ArrayList<String> cmdList = new ArrayList<>();
        String curr = "";
        boolean text = false;
        char qualifier = ' ';
        for(int i = 0; i < command.length(); i++) {
            char c = command.charAt(i);
            if(contains(c, textQualifiers)) {
                if(text && c == qualifier) {
                    text = false;
                }else {
                    text = true;
                    qualifier = c;
                }
            }else if(!text) {
                if(c == ' ') {
                    curr += c;
                    cmdList.add(curr);
                    curr = "";
                }
            }else {
                curr += c;
            }
        }
        String[] cmd = new String[cmdList.size()];
        for(int i = 0; i < cmd.length; i++) {
            cmd[i] = cmdList.get(i);
        }
        process(cmd);
    }

    private void process(String[] command) {

    }

    private boolean contains(char c, char[] arr) {
        for(char s : arr) {
            if(c == s) return true;
        }
        return false;
    }
}
