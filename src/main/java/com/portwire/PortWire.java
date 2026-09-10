package com.portwire;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PortWire {

    public PortWire() throws IOException {

        // Creates the port of connection
        try (ServerSocket serverSocket = new ServerSocket(8080)) {

            // accepts the connection with a client
            Socket socket = serverSocket.accept();

        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
