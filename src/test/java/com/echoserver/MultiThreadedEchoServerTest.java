package com.echoserver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiThreadedEchoServerTest {

    private static MultiThreadedEchoServer server;
    private static Thread serverThread;
    private static final int PORT = 9090;

    @BeforeAll
    public static void startServer() {
        server = new MultiThreadedEchoServer(PORT);
        serverThread = new Thread(server::start);
        serverThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void stopServer() {
        server.stop();
        try {
            serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSingleClientEcho() throws Exception {
        try (Socket clientSocket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String message = "Hello, Echo Server!";
            out.println(message);

            String response = in.readLine();
            assertEquals(message, response);
        }
    }

    @Test
    public void testMultipleClients() throws Exception {
        try (Socket clientSocket1 = new Socket("localhost", PORT);
             PrintWriter out1 = new PrintWriter(new OutputStreamWriter(clientSocket1.getOutputStream()), true);
             BufferedReader in1 = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));

             Socket clientSocket2 = new Socket("localhost", PORT);
             PrintWriter out2 = new PrintWriter(new OutputStreamWriter(clientSocket2.getOutputStream()), true);
             BufferedReader in2 = new BufferedReader(new InputStreamReader(clientSocket2.getInputStream()))) {

            String message1 = "Client 1 Message";
            String message2 = "Client 2 Message";

            out1.println(message1);
            out2.println(message2);

            String response1 = in1.readLine();
            String response2 = in2.readLine();

            assertEquals(message1, response1);
            assertEquals(message2, response2);
        }
    }

    @Test
    public void testExitCommand() throws Exception {
        try (Socket clientSocket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String message = "exit";
            out.println(message);

            String response = in.readLine();
            assertEquals(message, response);
        }
    }
}
