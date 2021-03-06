package com.jmaerte.util.input.commands;

import com.jmaerte.util.input.Table;
import com.jmaerte.util.input.commands.collection.*;
import com.jmaerte.util.log.Logger;

import java.util.HashMap;
import java.util.Map;

public class Commands {

    private static final HashMap<String, Command> commands = new HashMap<>();

    static {
        commands.put("help", new Help());
        commands.put("<-", new AssignObject());
        commands.put("lo", new ListObjects());
        commands.put("clear", new Clear());
        commands.put("plot", new Plot());
        commands.put("finalize", new FinalizeObject());
        commands.put("save", new SaveObject());
        commands.put("home", new SetHome());
    }

    public static void print() {
        Table t = new Table(new int[]{20, 15, 15, 40}, new String[]{"Syntax", "Options", "Modifiers", "Description"});
        for(Command in : commands.values()) {
            t.addEntry(new String[]{in.syntax(), in.option(), in.modifier(), in.description()});
            t.addEmpty();
        }
        t.addBorder();
        System.out.println(t);
    }

    public static void process(String[] command) {
        for(Map.Entry<String, Command> com : commands.entrySet()) {
            try {
                if(com.getValue().command().equals(command[com.getValue().positionCommand()])) {
                    String[] params = new String[command.length - 1];
                    System.arraycopy(command, 0, params, 0, com.getValue().positionCommand());
                    System.arraycopy(command, com.getValue().positionCommand() + 1, params, com.getValue().positionCommand(), command.length - com.getValue().positionCommand() - 1);
                    Logger.printToLog(com.getValue().call(params));
                    return;
                }
            }catch(Exception e) {}
        }
        System.out.println("Unknown command or it does not match any syntax.");
        Logger.printToLog("Unknown command or it does not match any syntax.");
    }

}
