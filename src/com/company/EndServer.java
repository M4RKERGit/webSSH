package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EndServer
{
    static String browseText = "Welcome to webSSH\n";
    static String htmlCode = "";
    public static void craftServer()
    {
        try (ServerSocket serverSocket = new ServerSocket(80)) {
            Additional.appendLog("Server started!");
            String request = "", fullBuffer = "";
            while (true)
            {
                Socket socket = serverSocket.accept();
                Additional.appendLog("Client connected!");
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream()))
                {
                    while (!input.ready());
                    if (input.ready()) {request = input.readLine();}
                    while (input.ready()) {fullBuffer += (input.readLine() + "\n");}
                    System.out.println(fullBuffer);
                    parseRequest(request);
                    refreshHTML();
                    output.println(htmlCode);
                    Additional.appendLog("Client disconnected!");
                }
            }
        }
        catch (IOException ex) {Additional.appendLog("Connection error");}
    }

    private static void parseRequest(String req) throws IOException {
        String[] parsed = req.split(" ");
        String mode = parsed[0];
        String command = parsed[1].replaceAll("%20", " ");
        if (command.equals("/favicon.ico"))  return;
        switch (mode)
        {
            case "GET":
                Additional.appendLog("Got GET Request");
                break;
            case "POST":
                Additional.appendLog("Got POST request with " + command);
                if (command.equals("/clear")) {browseText = ""; return;}
                browseText += Additional.executeUtil(command.substring(1, command.length()));
                Additional.appendLog("Invalid command");
                break;
        }
    }

    private static void refreshHTML()
    {
        htmlCode = "<!DOCTYPE HTML>\n" +
                "<html>\n" +
                "<head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <form>\n" +
                "        <p><textarea style = \"width: 90%; height: 800px;\" name=\"message\">"+browseText+"</textarea></p>\n" +
                "        <p><input style = \"width: 50%;\" id=\"txtLine\" type=\"txtLine\" name=\"txtLine\" value=\"\"></p>\n" +
                "        <button onclick=\"sendCMD()\">Send</button>\n" +
                "    </form>\n" +
                "    <script>\n" +
                "        function sendCMD() \n" +
                "        {\n" +
                "        var val = document.getElementById(\"txtLine\").value;\n" +
                "        var xhr = new XMLHttpRequest();\n" +
                "        xhr.open('POST', val, false);\n" +
                "        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');\n" +
                "        xhr.send(val);\n" +
                "        }\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";
    }
}
