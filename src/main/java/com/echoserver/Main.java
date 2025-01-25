package com.echoserver;


public class Main {

    public static void main(String[] args) {
        int port = 9090;
        MultiThreadedEchoServer server = new MultiThreadedEchoServer(port);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop)); // Остановка сервера при завершении программы

        server.start();
    }
}
