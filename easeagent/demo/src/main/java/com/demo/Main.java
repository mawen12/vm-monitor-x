package com.demo;

public class Main {

    // java -javaagent:/home/mawen/Documents/github/mawen12/vm-monitor-x/easeagent/core/target/easeagent-dep.jar
    public static void main(String[] args) throws ClassNotFoundException {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.sayHello("mawen");

//        Class.forName("com.github.mawen12.easeagent.api.metrics.Timer");
    }
}
