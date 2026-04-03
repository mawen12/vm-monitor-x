package com.github.mawen12.utils;

import com.github.mawen12.model.Monitorable;

import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.Duration;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SocketWriter implements Runnable {
    private static final Duration internal = Duration.ofSeconds(2);

    private final int port;
    public Monitorable monitorable;

    public SocketWriter(int port, Monitorable monitorable) {
        this.port = port;
        this.monitorable = monitorable;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket("127.0.0.1", port);
             OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), UTF_8)) {

            if (socket.isConnected()) {
                String message;
                try {
                    message = String.format("A:%s", monitorable.getAbilities().toJson());
                } catch (Exception e) {
                    System.err.println("Error getting message: " + e.getClass() + " " + e.getMessage());
                    e.printStackTrace();
                    return;
                }

                System.out.println("Send message: " + message);

                writeMessage(osw, message);
            }

            while (socket.isConnected()) {
                String message;
                try {
                    message = String.format("M:%s", monitorable.getMetrics().toJson());
                } catch (Exception e) {
                    System.err.println("Error getting message: " + e.getClass() + " " + e.getMessage());
                    e.printStackTrace();
                    Thread.sleep(internal.toMillis());
                    continue;
                }

                System.out.println("Send message: " + message);

                writeMessage(osw, message);
                Thread.sleep(internal.toMillis());
            }

        } catch (SocketException socketEx) {
            System.out.println("Disconnected." + socketEx.getMessage());
        } catch (Exception ex) {
            System.err.println("Socket write error: " + ex.getClass() + " " + ex.getMessage());
        }
    }

    private void writeMessage(OutputStreamWriter osw, String message) throws Exception {
        osw.write(message + "\n");
        osw.flush();
    }
}
