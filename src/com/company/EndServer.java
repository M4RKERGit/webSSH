package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EndServer
{
    static String PRINT_PREFIX = " [SERV]: ";
    static String browseText = "Welcome to webSSH\n";
    static String htmlCode = "";
    public static void craftServer()
    {
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            Additional.appendLog("Server started!", PRINT_PREFIX);
            String request = "";
            while (true)
            {
                Socket socket = serverSocket.accept();
                //Additional.appendLog("Client connected!", PRINT_PREFIX);
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream()))
                {
                    while (!input.ready());
                    if (input.ready()) {request = input.readLine();}
                    Additional.appendLog(request, PRINT_PREFIX);
                    parseRequest(request);
                    htmlCode = HTMLCode.refreshHTML(browseText);
                    output.println(htmlCode);
                    //Additional.appendLog("Client disconnected!", PRINT_PREFIX);
                }
            }
        }
        catch (IOException ex) {Additional.appendLog("Connection error", PRINT_PREFIX);}
    }

    private static void parseRequest(String req)
    {
        String[] parsed = req.split(" ");
        String mode = parsed[0];
        String command = parsed[1].replaceAll("%20", " ");
        if (command.equals("/favicon.ico"))  return;
        switch (mode)
        {
            case "GET":
                Additional.appendLog("Got GET request", PRINT_PREFIX);
                return;
            case "POST":
                Additional.appendLog("Got POST request with " + command, PRINT_PREFIX);
                try {if (command.equals("/clear")) {browseText = "";return;} browseText += Additional.executeUtil(command.substring(1));}
                catch (IOException e) {System.out.println("Invalid command");}
                break;
        }
    }
}