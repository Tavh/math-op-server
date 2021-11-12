package com.beachbumtask.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MathServer {
    private MathServer() {}

    private static MathServer instance = new MathServer();

    public static MathServer getInstance() {
        if (instance == null) {
            synchronized (MathServer.class) {
                if (instance == null) {
                    instance = new MathServer();
                }
            }
        }
        return instance;
    }

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private final List<MathOperationsThread> mathClientThreads = new ArrayList<>();

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Math server is up on port: " + System.getenv("PORT"));
        } catch (IOException e) {
            System.out.println("I/O error: " + e);
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.out.println("I/O error: " + e);
            }
            MathOperationsThread socketWrappingThread = new MathOperationsThread(clientSocket);
            socketWrappingThread.start();
            mathClientThreads.add(socketWrappingThread);
        }
    }
}
