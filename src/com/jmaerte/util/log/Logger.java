package com.jmaerte.util.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public static final DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd - HH mm ss");

    public static final Date date = new Date();
    public static final boolean log = true;

    private static boolean progressing = false;
    private static int maxProgress;

    /**Reports warnings in following syntax:
     * [Log] - {@Code warn}
     *
     * @param warn message to warn about
     */
    public static void warn(String warn) {
        if(!log || progressing) return;
        System.out.println("[Warn] - " + warn);
    }

    /**Logs a message {@code info} in the following syntax:
     * [Info] - {@Code info}
     *
     * @param info message to log
     */
    public static void info(String info) {
        if(!log || progressing) return;
        System.out.println("[Info] - " + info);
    }

    public static void log(String msg) {
        if(!log || progressing) return;
        System.out.println(msg);
    }

    public static void updateProgress(int progress) {
        int bars = (progress * 10)/maxProgress;
        String bar = "";
        for(int i = 0; i < bars; i++) bar += "=";
        for(int i = 10; i > bars; i--) bar += " ";
        System.out.print("[" + bar + "] ~ " + progress + "/" + maxProgress + "\r");
    }

    public static void progress(int max, String progressName) {
        progressing = true;
        maxProgress = max;
        System.out.println(progressName + ":");
        System.out.print("[          ] ~ 0/" + max + "\r");
    }

    public static void close() {
        progressing = false;
        System.out.print("[==========] ~ " + maxProgress + "/" + maxProgress + "\n\n");
    }
}
