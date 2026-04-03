package com.github.mawen12.core;

import com.github.mawen12.model.Monitorable;
import com.github.mawen12.utils.SocketWriter;

public class Server implements Runnable {
    private final int port;
    private final Monitorable monitor = new Monitor();

    public Server(int port) throws Exception {
        this.port = port;
    }

    @Override
    public void run() {
        SocketWriter socketWriter = new SocketWriter(port, monitor);
        socketWriter.run();
    }
}