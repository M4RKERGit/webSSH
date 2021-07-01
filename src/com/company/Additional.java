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
    public static void appendLog(String text)
    {
        String log = new Date() + " Received: " + text;// + " " + "From: " + user;
        System.out.println(log);
        try {Files.write(Path.of("log.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});}
        catch (IOException e) {System.out.println("Logging error");}
    }

    public static String executeUtil(String line)
    {
        Process process = null;
        try{process = Runtime.getRuntime().exec(line);}
        catch (IOException e){Additional.appendLog("Command execution error");}
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s = "", report = "";
        while (true)
        {
            try {if (!((s = stdInput.readLine()) != null)) break;}
            catch (IOException e){Additional.appendLog("Unable to read line");}
            report += (s + "\n");
        }
        try {process.waitFor();}
        catch (InterruptedException e){Additional.appendLog("Process crush");}
        return report;
    }
}
