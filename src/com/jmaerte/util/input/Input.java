package com.jmaerte.util.input;

import com.jmaerte.data_struc.complex.Filtration;
import com.jmaerte.data_struc.point_set.Landmarks;
import com.jmaerte.data_struc.point_set.PointSet;
import com.jmaerte.util.input.commands.Commands;
import com.jmaerte.util.vector.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Input {

    private static String[] currCommand;
    public static HashMap<String, String> options;
    private static ArrayList<Modifier> attributes;

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
        Commands.process(currCommand);
        input();
    }

    private static void options() {
        for(int i = 0; i < currCommand.length; i++) {
            if(currCommand[i].startsWith("--")) {
                try {
                    attributes.add(getModifier(currCommand[i].substring(2)));
                }catch(Exception e) {
                    System.err.println(e.getMessage());
                }
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

    private static Modifier getModifier(String mod) throws Exception {
        for(Modifier m : Modifier.values()) {
            if(m.mod.equals(mod)) {
                return m;
            }
        }
        throw new Exception("\"--" + mod + "\" is not a valid modifier.");
    }

    private static void process() {
        if(currCommand.length == 0) return;
        switch(currCommand[0]) {
            case "help":
                if(currCommand.length == 1) {
                    HELP();
                }else {
                    INFO(currCommand[1]);
                }
                break;
            case "free_mem":
                Register.free();
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
                        if (v.getSecond() == null) {
                            System.out.println("Initialization failed.");
                        } else {
                            Register.push(currCommand[0], v.getFirst(), v.getSecond());
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }else {
                    try {
                        Register.describe(currCommand[0]);
                    } catch(Exception e) {
                        System.out.println("Unknown Command or Object.");
                    }
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
                if(is(Modifier.CSV)) {
                }
                break;
            case "Landmarks":
                Vector2D<Object, Class> w = Register.get(options.get("set"));
                PointSet M = (PointSet) w.getFirst();
                int n = Integer.valueOf(currCommand[3]);
                result = new Vector2D<>(n + " " + (is(Modifier.MAXMIN) ? "per maxmin" : "randomly") + " chosen Landmark Points of " + options.get("set"),
                        new Landmarks(M, n, is(Modifier.MAXMIN)));
                break;
            case "Filtration":
                Vector2D<Object, Class> v = Register.get(options.get("set"));
                Landmarks S = (Landmarks) v.getFirst();
                int k = Integer.valueOf(currCommand[3]);
                switch(options.get("t")) {
                    case "cech":
                        result = new Vector2D<>(k + "-Skeleton of Čech(" + options.get("set") + ")", Filtration.cech(S, k));
                        break;
                    case "viet":
                        result = new Vector2D<>(k + "-Skeleton of Viet(" + options.get("set") + ")", Filtration.vietoris(S, k));
                        break;
                    case "witness":
                        result = new Vector2D<>(k + "-Skeleton of Witness_lazy(" + options.get("set") + ")", Filtration.witness_lazy(S, k));
                        break;
                    default:
                        throw new Exception("The program doesn't support " + options.get("t") + " filtrations.");
                }
                break;
        }
        return result;
    }

    public static void main(String... args) {
        System.out.println("Copyright 2018, Julian Märte, All rights reserved.\n" +
                "Calculator for Persistent Homology of Metric Point Sets.\n" +
                "Having questions or suggestions? Please send them to maertej@students.uni-marburg.de!\n" +
                "Use `help` for an overview of commands.");
        input();
    }

    public static boolean is(Modifier attribute) {
        for(Modifier att : attributes) {
            if(att == attribute) return true;
        }
        return false;
    }

    private static void HELP() {
        System.out.println("+-------------------+----------------------------------------------+\n" +
                "|      Syntax       |                 Explanation                  |\n" +
                "+-------------------+----------------------------------------------+\n" +
                "| help              | Shows this dialog                            |\n" +
                "|                   |                                              |\n" +
                "| help [command]    | Shows a help dialog according to [command],  |\n" +
                "|                   |     where [command] is one of the following: |\n" +
                "|                   |     -objects: Help initializing objects      |\n" +
                "|                   |                                              |\n" +
                "| li                | List all information keywords                |\n" +
                "|                   |                                              |\n" +
                "| [var] <- [object] | Assign the [object] to variable name [var]   |\n" +
                "|                   |     In case you need more information on how |\n" +
                "|                   |     to init an object type \"info object\"     |\n" +
                "|                   |                                              |\n" +
                "| lo                | List all Objects specified by the user       |\n" +
                "|                   |                                              |\n" +
                "| free_mem          | Free Memory by deleting user specified vars  |\n" +
                "+-------------------+----------------------------------------------+");
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
                        "|                |                                    |       type \"help PointSet\".                            |\n" +
                        "|                |                                    |                                                        |\n" +
                        "| Filtration [k] | Creates the k-Skeleton of          | -t [type]: Filtration type(cech, viet, witness)        |\n" +
                        "|                | a specified Filtration             | -set a PointSet object to build the Filtration from    |\n" +
                        "|                |                                    |                                                        |\n" +
                        "+----------------+------------------------------------+--------------------------------------------------------+");
                break;
            case "Filtration":

                break;
        }
    }
}
