package com.beachbumtask.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * A math operation server class, implements the singleton pattern as only 1 server should exists per application
 * this class instantiates different threads who handle different clients
 */
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

    /**
     * A start function that initializes the server in the specified port, loops while waiting for clients
     * to try to connect, creates a new thread for each new client
     */
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

    /**
     * A stop function that kills all threads in the server before shutting down the server (unused as of now)
     */
    public void stop() {
        mathClientThreads.forEach(MathOperationsThread::kill);
    }
}
