package com.beachbumtask.server;

import com.beachbumtask.exceptions.InvalidCommandException;
import com.beachbumtask.math.operations.MathOperation;
import com.beachbumtask.math.operations.factory.MathOperationFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static com.beachbumtask.constants.ProtocolConstants.QUIT_COMMAND;
import static com.beachbumtask.constants.ProtocolConstants.RESPONSE_END;

/**
 * A math operation thread, designed to provide services for a single math operation client
 */
public class MathOperationsThread extends Thread {
    private final Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public MathOperationsThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * An init function that establishes the i/o of the agent and starts the liveness timer
     */
    private void init() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            System.out.println("Established client connection");
            runLivenessPing();
        } catch (IOException e) {
            System.out.println("Failed to initialize connection");
        }
    }

    /**
     * @param messages an indefinite number of messages to the client
     */
    private void sendResponse(String... messages) {
        Arrays.stream(messages).forEach(message ->
            out.println(message)
        );
        out.println(RESPONSE_END);
        out.flush();
    }

    private void sendInfo(String... messages) {
        Arrays.stream(messages).forEach(message ->
                out.println("INFO: " + message)
        );
        out.println(RESPONSE_END);
        out.flush();
    }

    /**
     * Waits for requests for the client
     * @return a command from the client
     */
    private String handleRequest() throws IOException{
        String line;
        while((line = in.readLine()).equals("")) {}
        return line;
    }

    /**
     * Starts a scheduled task that sends a liveness to the client every 60 seconds
     */
    private void runLivenessPing() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            public void run() {
                System.out.println("Running liveness");
                sendInfo("Still alive");
            }
        }, 0, 60000);
    }

    /**
     * The main loop of the client, keeps getting requests from the client, parsing them into commands, performing
     * operations and sending a response
     */
    private void eventLoop() {
        while (true) {
            try {
                String unparsedCommand = handleRequest();
                System.out.println("Received command: " + unparsedCommand);
                if (QUIT_COMMAND.equalsIgnoreCase(unparsedCommand)) { kill(); }
                try {
                    final MathOperation mathOperation = MathOperationFactory
                            .getOperationsFactory()
                            .getOperation(unparsedCommand);
                    sendResponse("your result is: " + mathOperation.perform(unparsedCommand));
                } catch (InvalidCommandException e) {
                    sendResponse(e.getMessage());
                }

            } catch (SocketException e) {
                this.kill();
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Kills the thread, ending the connection with the client
     */
    public void kill() {
        System.out.println("Client connection closed");
        this.interrupt();
    }

    /**
     * An override of the Thread.run method, initializes the agent and starts the event loop
     */
    @Override
    public void run() {
        init();
        eventLoop();
    }
}
