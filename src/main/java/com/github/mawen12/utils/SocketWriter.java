package com.github.mawen12.utils;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.Duration;
import java.util.concurrent.Callable;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SocketWriter implements Runnable {
    private int port;

    private Duration internal = Duration.ofSeconds(2);

    private Callable<String> messageSupplier;

    public SocketWriter(int port, Callable<String> messageSupplier) {
        this.port = port;
        this.messageSupplier = messageSupplier;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("127.0.0.1", port);
             OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), UTF_8)) {

            while (socket.isConnected()) {
                String message;
                try {
                    message = messageSupplier.call();
                } catch (Exception e) {
                    System.err.println("Error getting message: " + e.getClass() + " " + e.getMessage());
                    e.printStackTrace();
                    Thread.sleep(internal.toMillis());
                    continue;
                }

                osw.write(message + "\n");
                osw.flush();
                Thread.sleep(internal.toMillis());
            }

        } catch (SocketException socketEx) {
            System.out.println("Disconnected." + socketEx.getMessage());
        } catch (Exception ex) {
            System.err.println("Socket write error: " + ex.getClass() + " " + ex.getMessage());
        }
    }
}
