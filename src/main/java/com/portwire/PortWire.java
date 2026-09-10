package com.portwire;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class PortWire {

    public PortWire() throws IOException {

        // Creates the port of connection
        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            // accepts the connection with a client
            Socket socket = serverSocket.accept();

            // get client input
            InputStream inputStream = socket.getInputStream();

            // send client the output
            OutputStream outputStream = socket.getOutputStream();

        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
