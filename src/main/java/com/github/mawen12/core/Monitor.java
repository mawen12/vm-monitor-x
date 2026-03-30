package com.github.mawen12.core;

import com.github.mawen12.model.JMemory;
import com.github.mawen12.model.JThread;
import com.github.mawen12.model.Metrics;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.lang.management.ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME;

public class Monitor {
    final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    final Map<Long, JThread> threads = new HashMap<>();

    public String getMetricsJson() throws Exception {
        return getMetrics().toJson();
    }

    public Metrics getMetrics() throws Exception {

        Metrics metrics = new Metrics();
        metrics.Memory = getMemory();
        metrics.Load = getProcessCpuLoad();
        metrics.Time = LocalDateTime.now().getNano();

        return metrics;
    }

    public JMemory getMemory() {
        Runtime rt = Runtime.getRuntime();
        long usedMem = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        long maxMem = rt.maxMemory() / 1024 / 1024;

        JMemory memory = new JMemory();
        memory.Used = usedMem;
        memory.Max = maxMem;
        return memory;
    }

    public double getProcessCpuLoad() throws Exception {
        ObjectName osObj = ObjectName.getInstance(OPERATING_SYSTEM_MXBEAN_NAME);
        AttributeList osAttrs = mbs.getAttributes(osObj, new String[]{"ProcessCpuLoad"});

        if (osAttrs.isEmpty())
            return 0;

        Attribute att = (Attribute) osAttrs.get(0);
        Double value = (Double) att.getValue();

        // usually takes a couple of seconds before we get real values
        if (value == -1.0)
            return 0;
        // returns a percentage value with 1 decimal point precision
        return Math.round((value * 1000) / 10.0 * 100) / 100.0;
    }
}
