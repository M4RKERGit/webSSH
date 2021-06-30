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
    public static void appendLog(String text) throws IOException
    {
        String log = new Date() + " Received: " + text;// + " " + "From: " + user;
        System.out.println(log);
        Files.write(Path.of("log.txt"), (log + "\n").getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
    }

    public static String executeUtil(String line) throws IOException, InterruptedException
    {
        Process process = Runtime.getRuntime().exec(line);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String s, report = "";
        while ((s = stdInput.readLine()) != null)
        {
            report += (s + "\n");
        }
        process.waitFor();
        return report;
    }
}
