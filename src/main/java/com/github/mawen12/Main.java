package com.github.mawen12;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static class LargeObject {
        byte[] memory = new byte[1024 * 1024];  // 1MB
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                List<LargeObject> largeObjects = new ArrayList<>();
                for (int j = 0; j < 200; j++) {
                    largeObjects.add(new LargeObject());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println("Finished thread " + Thread.currentThread());
            }, "testThread_" + i + "_with_potential_long_name").start();
        }

        // 移除注释，并填上 go 申请的端口号，以便测试 Java Agent 的通信功能
//        new Thread(new Server(37271), Server.class.getName()).start();

        System.out.println("Waiting for input => ");
        System.in.read();
    }
}
