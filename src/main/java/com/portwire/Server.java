package com.portwire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final int port;
    private final Router router;

    public Server(int port) {
        this.port = port;
        this.router = new Router();
    }

    public void start() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("PortWire running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        }
    }

    private void handleClient(Socket socket) {
        try (socket) {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream(),
                            StandardCharsets.UTF_8
                    )
            );

            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            String[] parts = requestLine.split(" ");
            if (parts.length != 3) {
                socket.getOutputStream().write(
                        HttpResponse.badRequest().toBytes()
                );
                return;
            }

            Map<String, String> headers = readHeaders(reader);

            HttpRequest request = new HttpRequest(
                    parts[0], parts[1], parts[2], headers
            );

            System.out.println("→ " + request);

            HttpResponse response = router.route(request);
            socket.getOutputStream().write(response.toBytes());
            socket.getOutputStream().flush();

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }

    private Map<String, String> readHeaders(BufferedReader reader)
            throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) break;

            int colonIndex = line.indexOf(':');
            if (colonIndex == -1) continue;

            String name  = line.substring(0, colonIndex).trim().toLowerCase();
            String value = line.substring(colonIndex + 1).trim();
            headers.put(name, value);
        }

        return headers;
    }
}
