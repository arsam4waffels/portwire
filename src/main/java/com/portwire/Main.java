package com.portwire;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <h5>PortWire Project</h5>
 * <p>A mini-project to understand web server concepts and how networks,
 * servers, and ports communicate using streams and sockets.</p>
 * @since 2026
 * @version 1.0
 * @author Arsam
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // PortWire portWire = new PortWire();
        Server server = new Server(8080);
        server.start();
    }
}