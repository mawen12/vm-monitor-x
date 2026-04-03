package com.github.mawen12;

import com.github.mawen12.core.Server;

import java.lang.instrument.Instrumentation;

public class Bootstrap {

    public static void agentmain(String args, Instrumentation instrumentation) throws Exception {
        System.out.println("Receive args is " + args);
        int port = Integer.parseInt(args);

        new Thread(new Server(port), Server.class.getName()).start();
    }
}
