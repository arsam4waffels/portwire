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
            while (true) handelClient(serverSocket.accept());
        } catch (IOException e) {
            System.out.println(
                    "Server error: " + e.getMessage()
            );
        }
    }

    private static void handelClient(Socket socket) {
        try (socket) {
            InputStream inputStream = socket.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream,
                    StandardCharsets.UTF_8
            );
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader
            );

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.isEmpty()) break;
            }
            sendResponse(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void sendResponse(Socket socket) throws IOException {
        String body = "Oreo said Meow";
        byte[] bodyByte = body.getBytes(
                StandardCharsets.UTF_8
        );
        // I have no idea what these are.
        // I only got them when running the server port.
        String responseHeaders =
                "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: text/plain; charset=UTF-8\r\n"
                        + "Content-Length: " + bodyByte.length + "\r\n"
                        + "Connection: close\r\n"
                        + "\r\n";

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(
                responseHeaders.getBytes(
                        StandardCharsets.UTF_8
                )
        );
        outputStream.write(bodyByte);
        outputStream.flush();
    }
}
