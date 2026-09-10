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
        // Creates the port of connection
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                // accepts the connection with a client
                try (Socket socket = serverSocket.accept()) {
                    // get client input
                    InputStream inputStream = socket.getInputStream();

                    int data;
                    while ((data = inputStream.read()) != -1) {
                        System.out.println((char) data);
                    }
                    // reads client input
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream, StandardCharsets.UTF_8
                    );
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    // send client the output
                    OutputStream outputStream = socket.getOutputStream();
                }
            }
        }
    }
}
