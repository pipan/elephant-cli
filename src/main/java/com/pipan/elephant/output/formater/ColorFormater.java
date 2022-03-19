package com.pipan.elephant.output.formater;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class ColorFormater implements Formater {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    private Map<String, String> colors;

    public ColorFormater() {
        this.colors = new Hashtable();
        this.colors.put("green", ColorFormater.GREEN);
        this.colors.put("red", ColorFormater.RED);
        this.colors.put("blue", ColorFormater.BLUE);
        this.colors.put("yellow", ColorFormater.YELLOW);
    }

    public String format(String message) {
        Iterator<String> i = this.colors.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next();
            message = message.replace("<" + key + ">", this.colors.get(key));
            message = message.replace("</" + key + ">", ColorFormater.RESET);
        }
        return message;
    }
}