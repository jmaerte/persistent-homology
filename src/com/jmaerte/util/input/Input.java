package com.jmaerte.util.input;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.vector.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Input {

    private static String[] currCommand;
    private static HashMap<String, String> options;
    private static ArrayList<String> attributes;

    private static final String CSV = "csv", MINMAX = "minmax";

    private static final Scanner scanner;

    static {
        scanner = new Scanner(System.in);
    }

    private static void input() {
        String next = scanner.nextLine();
        ArrayList<String> cmd = new ArrayList<>();
        boolean text = false;
        String curr = "";
        for(char c : next.toCharArray()) {
            if(c == '\"') {
                text = !text;
            }else if(!text && c == ' ') {
                cmd.add(curr);
                curr = "";
            }else {
                curr += c;
            }
        }
        if(!curr.equals("")) {
            cmd.add(curr);
        }
        currCommand = cmd.toArray(new String[cmd.size()]);
        options = new HashMap<>();
        attributes = new ArrayList<>();
        options();
        process();
        input();
    }

    private static void options() {
        for(int i = 0; i < currCommand.length; i++) {
            if(currCommand[i].startsWith("--")) {
                attributes.add(currCommand[i].substring(2));
                String[] cmd = new String[currCommand.length - 1];
                System.arraycopy(currCommand, 0, cmd, 0, i);
                if(i + 1 != currCommand.length) {
                    System.arraycopy(currCommand, i + 1, cmd, i, currCommand.length - i - 1);
                }
                i--;
                currCommand = cmd;
            }else if(currCommand[i].startsWith("-")) {
                options.put(currCommand[i].substring(1), currCommand[i+1]);
                String[] cmd = new String[currCommand.length - 2];
                System.arraycopy(currCommand, 0, cmd, 0, i);
                if(i + 2 != currCommand.length) {
                    System.arraycopy(currCommand, i + 2, cmd, i, currCommand.length - i - 2);
                }
                i-=2;
                currCommand = cmd;
            }
        }
    }

    private static void process() {
        if(currCommand.length == 0) return;
        switch(currCommand[0]) {
            case "help":
                HELP();
                break;
            case "free_mem":
                Register.free();
                break;
            case "info":
                INFO(currCommand[1]);
                break;
            case "lo":
                System.out.println(Register.print());
                break;
            case "li":
                //List all info-keywords.
                break;
            default:
                if(currCommand[1].equals("<-")) {
                    try {
                        Vector2D<String, Object> v = processInitialization();
                        if(v.getSecond() == null) {
                            System.out.println("Initialization failed.");
                        }else {
                            Register.push(currCommand[0], v.getFirst(), v.getSecond());
                        }
                    } catch(Exception e) {
                        System.out.println(e.getMessage());
                    }
                }else {
                    System.out.println("Unknown Command.");
                }
        }
    }

    /**This method returns an auto generated description for an object (f.e. if a filtration was specified to be the
     * cech filtration of a given PointSet-object S, description would be "Cech-Filtration of S")
     * and a according to the user input initialised object.
     *
     */
    private static Vector2D<String, Object> processInitialization() throws Exception {
        Vector2D<String, Object> result = null;
        switch(currCommand[2]) {
            case "PointSet":
                result = new Vector2D<>("", null);
                if(is(CSV)) {
                }
                break;
            case "Landmarks":
                Vector2D<Object, Class> w = Register.get(options.get("set"));
                PointSet M = (PointSet) w.getFirst();
                int n = Integer.valueOf(currCommand[3]);
                result = new Vector2D<>(n + " " + (is(MINMAX) ? "per minmax" : "randomly") + " chosen Landmark Points of " + options.get("set"),
                        new Landmarks(M, n, is(MINMAX)));
                break;
            case "Filtration":
                Vector2D<Object, Class> v = Register.get(options.get("set"));
                PointSet S = (PointSet) v.getFirst();
                int k = Integer.valueOf(currCommand[3]);
                switch(options.get("t")) {
                    case "cech":
                        result = new Vector2D<>(k + "-Skeleton of Čech(" + options.get("set") + ")", Filtration.cech(S, k));
                        break;
                    case "viet":
                        result = new Vector2D<>(k + "-Skeleton of Viet(" + options.get("set") + ")", Filtration.vietoris(S, k));
                        break;
                    case "witness":
                        result = new Vector2D<>(k + "-Skeleton of Witness_lazy(" + options.get("set") + ")", Filtration.vietoris(S, k));
                        break;
                    default:
                        throw new Exception("The program doesn't support " + options.get("t") + " filtrations.");
                }
                break;
        }
        return result;
    }

    public static void main(String... args) {
        input();
    }

    private static boolean is(String attribute) {
        for(String att : attributes) {
            if(att.equals(attributes)) return true;
        }
        return false;
    }

    private static void HELP() {
        System.out.println("+-------------------+---------------------------------------------+\n" +
                "|      Syntax       |                 Explanation                 |\n" +
                "+-------------------+---------------------------------------------+\n" +
                "| help              | Shows this dialog                           |\n" +
                "|                   |                                             |\n" +
                "| info [keyword]    | print out information for a certain command |\n" +
                "|                   |   (keywords are given in the help overview) |\n" +
                "|                   |                                             |\n" +
                "| li                | List all information keywords               |\n" +
                "|                   |                                             |\n" +
                "| [var] <- [object] | Assign the [object] to variable name [var]  |\n" +
                "|                   |   In case you need more information on how  |\n" +
                "|                   |   to init an object type \"info object\"      |\n" +
                "|                   |                                             |\n" +
                "| lo                | List all Objects specified by the user      |\n" +
                "|                   |                                             |\n" +
                "| free_mem          | Free Memory by deleting user specified vars |\n" +
                "+-------------------+---------------------------------------------+");
    }
    private static void INFO(String subject) {
        switch(subject) {
            case "objects":
                System.out.println("+----------------+------------------------------------+--------------------------------------------------------+\n" +
                        "|     Syntax     |                 Object             |                          Options                       |\n" +
                        "+----------------+------------------------------------+--------------------------------------------------------+\n" +
                        "| PointSet       | Creates a PointSet object,         | -csv [path]: specifies from which file to parse        |\n" +
                        "|                | i.e. a collection of metric points |       the points in a csv-like manner                  |\n" +
                        "|                |                                    | -sample [type]: generates dummy data, sampled from     |\n" +
                        "|                |                                    |       specified type of structur. For more information |\n" +
                        "|                |                                    |       type \"info sample\".                              |\n" +
                        "|                |                                    |                                                        |\n" +
                        "| Filtration [k] | Creates the k-Skeleton of          | -t [type]: Filtration type(cech, viet, witness)        |\n" +
                        "|                | a specified Filtration             | -set a PointSet object to build the Filtration from    |\n" +
                        "|                |                                    |                                                        |\n" +
                        "+----------------+------------------------------------+--------------------------------------------------------+");
        }
    }
}
