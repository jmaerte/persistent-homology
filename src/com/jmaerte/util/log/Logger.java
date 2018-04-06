package com.jmaerte.util.log;

import java.util.Date;

public class Logger {

    public static final Date date = new Date();
    public static final boolean log = true;

    /**Reports warnings in following syntax:
     * [Log] - {@Code warn}
     *
     * @param warn message to warn about
     */
    public static void warn(String warn) {
        if(!log) return;
        System.out.println("[Warn] - " + warn);
    }

    /**Logs a message {@code info} in the following syntax:
     * [Info] - {@Code info}
     *
     * @param info message to log
     */
    public static void info(String info) {
        if(!log) return;
        System.out.println("[Info] - " + info);
    }

    public static void log(String msg) {
        if(!log) return;
        System.out.println(msg);
    }
}
