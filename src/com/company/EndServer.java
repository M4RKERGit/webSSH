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
            System.out.println("Server started!");
            String request = "";
            while (true)
            {
                Socket socket = serverSocket.accept();
                //System.out.println("Client connected!");
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream()))
                {
                    while (!input.ready());
                    if (input.ready())
                    {
                        request = input.readLine();
                    }
                    parseRequest(request);
                    refreshHTML();
                    output.println(htmlCode);
                    //System.out.println("Client disconnected!");
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void parseRequest(String req)
    {
        String[] parsed = req.split(" ");
        //for (int i = 0; i < parsed.length; i++) {System.out.println("Строка:" + parsed[i]);}
        String mode = parsed[0];
        String command = parsed[1];
        if (command.equals("/favicon.ico"))  return;
        switch (mode)
        {
            case "GET":
                System.out.println("Получен запрос GET");
                break;
            case "POST":
                System.out.println("Получен запрос POST с командой " + command);
                try
                {
                    if (command.equals("/clear"))
                    {
                        browseText = "";
                        return;
                    }
                    browseText += Additional.executeUtil(command.substring(1, command.length()));
                }
                catch (IOException | InterruptedException e)
                {
                    System.out.println("Ошибка ввода команды");
                }
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
                "        <button onclick=\"sendCMD()\">Загрузить шото</button>\n" +
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
