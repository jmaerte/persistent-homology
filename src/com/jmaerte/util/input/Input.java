package com.jmaerte.util.input;

import java.util.HashMap;
import java.util.Scanner;

public class Input {

    private String[] currCommand;
    private Scanner scanner;
    private HashMap<String, Object> map = new HashMap<>();

    public Input() {
        scanner = new Scanner(System.in);
    }

    private void input() {
        currCommand = scanner.next().split(" ");
        process();
        input();
    }

    private void process() {
        if(currCommand[1].equals("<-")) {

        }
    }
}
