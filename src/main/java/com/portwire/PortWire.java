package com.portwire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PortWire {

    public PortWire() throws IOException {
        final int PORT = 8080;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(
                    "Server is monitoring on port " + PORT
            );

            while (true) handleClient(serverSocket.accept());

        } catch (IOException e) {
            System.err.println(
                    "Server error: " + e.getMessage()
            );
        }
    }

    private static void handleClient(Socket socket) {
        try (socket) { // access to client

            // It reads the bytes coming from the client
            InputStream inputStream = socket.getInputStream();

            // Converts bytes to characters using UTF-8
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream,
                    StandardCharsets.UTF_8
            );

            // It makes working with text easier
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader
            );

            String requestLine = bufferedReader.readLine();

            if (requestLine == null || requestLine.isEmpty()) return;

            // It places each individual word into an array
            // ["GET", "/link", "HTTP/1.1"]
            String[] requestParts = requestLine.split(" ");

            if (requestParts.length != 3) {
                sendResponse(
                        socket,
                        400,
                        "Bad Request"
                );
                return;
            }

            String method   = requestParts[0];
            String path     = requestParts[1];
            String version  = requestParts[2];

            Map<String, String> headers = readHeaders(bufferedReader);

            System.out.println("Method: "   + method);
            System.out.println("Path: "     + path);
            System.out.println("Version: "  + version);

            for (Map.Entry<String, String> entry : headers.entrySet())
                System.out.println(
                        "Header: "
                                + entry.getKey()
                                + " = "
                                + entry.getValue()
                );

            String host = headers.get("host");

            String body =
                    "Method: " + method + "\n"
                            + "Path: " + path + "\n"
                            + "Version: " + version + "\n"
                            + "Host: " + host + "\n";

//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
//                if (line.isEmpty()) break;
//            }
            sendResponse(socket, 200, body);
        } catch (IOException e) {
            System.out.println(
                    "Server error: " + e.getMessage()
            );
        }
    }
    private static Map<String, String> readHeaders(
            BufferedReader reader
    ) throws IOException {

        Map<String, String> headers = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) break;

            int colonIndex = line.indexOf(':');
            if (colonIndex == -1) continue;

            String name = line
                    .substring(0, colonIndex)
                    .trim()
                    .toLowerCase();

            String value = line
                    .substring(colonIndex + 1)
                    .trim();

            headers.put(name, value);
        }

        return headers;
    }
    private static void sendResponse(
            Socket socket,
            int statusCode,
            String body
    ) throws IOException {
        String reasonPhrase = getReasonPhrase(statusCode);

        byte[] bodyBytes = body.getBytes(
                StandardCharsets.UTF_8
        );
        // I have no idea what these are.
        // I only got them when running the server port.
        String responseHeaders =
                "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/plain; charset=UTF-8\r\n"
                        + "Content-Length: " + bodyBytes.length + "\r\n"
                        + "Connection: close\r\n"
                        + "\r\n";

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(
                responseHeaders.getBytes(
                        StandardCharsets.UTF_8
                )
        );
        outputStream.write(bodyBytes);
        outputStream.flush();
    }
    private static String getReasonPhrase(
            int statusCode
    ) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            default -> "Unknown";
        };
    }
}
