package com.beachbumtask.server;

import com.beachbumtask.math.operations.MathOperation;
import com.beachbumtask.math.operations.factory.MathOperationFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.beachbumtask.constants.Commands.QUIT_COMMAND;

public class MathOperationsThread extends Thread {
    private final Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public MathOperationsThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    private void init() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            System.out.println("Established client connection");
        } catch (IOException e) {
            System.out.println("Failed to initialize connection");
        }
    }

    private void sendResponse(String... messages) {
        Arrays.stream(messages).forEach(message ->
            out.println(message)
        );
        endAndFlushOutput(out);
    }

    private void endAndFlushOutput(PrintWriter out) {
        out.println("<END>");
        out.flush();
    }

    private String handleRequest() throws IOException{
        String line;
        while((line = in.readLine()).equals("")) {}
        return line;
    }

    private void runLivenessPing() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("Running liveness");
                endAndFlushOutput(out);
            }
        }, 0, 60000);
    }

    private void eventLoop() {
        while (true) {
            runLivenessPing();
            try {
                String unparsedCommand = handleRequest();
                System.out.println("Received command: " + unparsedCommand);
                if (QUIT_COMMAND.equalsIgnoreCase(unparsedCommand)) {
                    kill();
                }
                final MathOperation mathOperation = MathOperationFactory
                        .getOperationsFactory()
                        .getOperation(unparsedCommand);

                sendResponse("your result is: " + mathOperation.perform(unparsedCommand));
            } catch (SocketException e) {
                this.kill();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void kill() {
        System.out.println("Client connection closed");
        this.interrupt();
    }

    @Override
    public void run() {
        init();
        eventLoop();
    }
}
