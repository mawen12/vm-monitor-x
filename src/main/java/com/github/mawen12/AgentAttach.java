package com.github.mawen12;

import com.sun.tools.attach.VirtualMachine;

public class AgentAttach {
    public static void main(String[] args) throws Exception {
        String pid = "12541";
        VirtualMachine vm = VirtualMachine.attach(pid);
//        vm.loadAgent("/home/mawen/Documents/github/mawen12/vm-monitor-x/build/libs/vm-monitor-x.jar=10");
        // 在启动 go 项目后，便会在 tmp 目录下有一个 vm-monitor-x.jar 文件，= 后面是 go 项目申请的端口号
        vm.loadAgent("/tmp/vm-monitor-x.jar2315077845=35617");
        vm.detach();
        System.out.println("Hello world");
    }
}
