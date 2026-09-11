package com.portwire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PortWire {

    public PortWire() throws IOException {
        final int PORT = 8080;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(
                    "Server is monitoring on port " + PORT
            );
            while (true) handleClient(serverSocket.accept());
        } catch (IOException e) {
            System.out.println(
                    "Server error: " + e.getMessage()
            );
        }
    }

    private static void handleClient(Socket socket) {
        try (socket) {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream,
                    StandardCharsets.UTF_8
            );
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader
            );

            String requestLine = bufferedReader.readLine();
            String[] requestParts = requestLine.split(" ");

            if (requestLine == null || requestLine.isEmpty()) return;

            System.out.println("Request line: " + requestLine);

            if (requestParts.length != 3) {
                sendBadRequest(socket);
                return;
            }

            String method = requestParts[0];
            String path = requestParts[1];
            String version = requestParts[2];

            System.out.println("Method: " + method);
            System.out.println("Path: " + path);
            System.out.println("Version: " + version);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.isEmpty()) break;
            }
            sendResponse(socket, method, path, version);
        } catch (IOException e) {
            System.out.println(
                    "Server error: " + e.getMessage()
            );
        }
    }
    private static void sendResponse(
            Socket socket,
            String method,
            String path,
            String version
    ) throws IOException {
        String body =
                "Method: " + method + "\n"
                + "Path: " + path + "\n"
                + "Version: " + version + "\n";
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
    private static void sendBadRequest(Socket socket) throws IOException {
        String err = "400 Bad Request";
        byte[] bodyBytes = err.getBytes(
                StandardCharsets.UTF_8
        );
        String response =
                "HTTP/1.1 400 Bad Request\r\n"
                        + "Content-Type: text/plain; charset=UTF-8\r\n"
                        + "Content-Length: " + bodyBytes.length + "\r\n"
                        + "Connection: close\r\n"
                        + "\r\n";
        OutputStream output =
                socket.getOutputStream();

        output.write(
                response.getBytes(
                        StandardCharsets.UTF_8
                )
        );

        output.write(bodyBytes);
        output.flush();
    }
}
