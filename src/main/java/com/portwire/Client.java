package com.portwire;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {
    public static void main(String[] args) throws IOException {

        try (
                Socket socket = new Socket("localhost", 8080)
        ) {

            OutputStream outputStream = socket.getOutputStream();

            byte[] message = "Oreo".getBytes(StandardCharsets.UTF_8);

            outputStream.write(message);

            InputStream inputStream = socket.getInputStream();

            int data;
            while ((data = inputStream.read()) != -1) {
                System.out.println(data);
            }

        } catch (IOException e) {
            throw new IOException(e);
        }

    }
}
