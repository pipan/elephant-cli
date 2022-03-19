package com.pipan.elephant.output;

import com.pipan.elephant.output.formater.ColorFormater;
import com.pipan.elephant.output.formater.Formater;
import java.util.List;
import java.util.LinkedList;

public class ConsoleOutput {
    private Integer lineCount = 0;
    private List<Formater> formaters;

    public ConsoleOutput() {
        this.formaters = new LinkedList();
        this.formaters.add(new ColorFormater());
    }

    public void write(String message) {
        this.output(message);
        this.lineCount++;
        
    }

    public void rewrite(String message) {
        if (this.lineCount == 0) {
            this.write(message);
            return;
        }
        System.out.print("\033[A\r\033[2K"); // move one line up and remove content on the line
        this.output(message);
    }

    private void output(String message) {
        for (Formater formater: this.formaters) {
            message = formater.format(message);
        }
        System.out.println(message);
    }
}