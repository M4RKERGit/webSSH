package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;

public class Additional
{
    static String PRINT_PREFIX = " [ADD]: ";
    static String[] unableToExec = {"cd"};
    public static void appendLog(String text, String prefix)
    {
        String log = new Date() + prefix + text;// + " " + "From: " + user;
        System.out.println(log);
        try {Files.write(Path.of("log.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});}
        catch (IOException e) {System.out.println("Logging error");}
    }

    public static String executeUtil(String line) throws IOException
    {
        for (int i = 0; i < unableToExec.length; i++) {if (line.split(" ")[0].equals(unableToExec[i])) return "This utility is unavaliable";}
        Process process = null;
        try {process = Runtime.getRuntime().exec(line);}
        catch (IOException e) {e.printStackTrace();}
        BufferedReader stdInput = null;
        if (process != null) {stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Additional.appendLog("Process started", PRINT_PREFIX);}
        String s = "", report = "";
        while (true)
        {
            try {if ((s = stdInput.readLine()) == null) break;}
            catch (IOException e){Additional.appendLog("Unable to read line", PRINT_PREFIX);}
            report += (s + "\n");
        }
        try {process.waitFor();}
        catch (InterruptedException e) {e.printStackTrace(); Additional.appendLog("Process crushed", PRINT_PREFIX);}
        Additional.appendLog("Process finished", PRINT_PREFIX);
        return report;
    }
}