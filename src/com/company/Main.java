package com.company;

public class Main
{
    static String PRINT_PREFIX = " [MAIN]: ";
    public static void main(String[] args)
    {
        Additional.appendLog("webSSH started...", PRINT_PREFIX);
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {Additional.appendLog("webSSH closed...Goodbye\nPress any key to close terminal", PRINT_PREFIX); new java.util.Scanner(System.in).nextLine();}));
        EndServer.craftServer();
    }
}