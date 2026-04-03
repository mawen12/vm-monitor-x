package com.github.mawen12.core;

import com.github.mawen12.model.*;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.management.ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME;

public class Monitor implements Monitorable {
    final MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    final Map<Long, JThread> threads = new HashMap<>();

    public boolean started;
    public Abilities abilities;

    public Monitor() throws Exception {
        check();
        this.started = true;
    }

    public void check() throws Exception {
        this.abilities = new Abilities();

        Set<ObjectName> names = mbs.queryNames(new ObjectName("com.alibaba.druid:type=DruidDataSource,*"), null);
        this.abilities.DataSources.addAll(names);

        names = mbs.queryNames(new ObjectName("Tomcat:type=ThreadPool,*"), null);
        if (names.size() == 1) {
            this.abilities.Tomcat = names.iterator().next();
            this.abilities.tomcatName = this.abilities.Tomcat.getKeyProperty("name");
        }
    }

    public Abilities getAbilities() throws Exception {
        return abilities;
    }

    public Metrics getMetrics() throws Exception {
        Metrics metrics = new Metrics();
        metrics.Memory = getMemory();
        metrics.Load = getProcessCpuLoad();
        metrics.DataSources = getDataSources();
        metrics.Tomcat = getTomcat();
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

    public Map<String, JDruidDataSource> getDataSources() throws Exception {
        if (abilities.DataSources.isEmpty()) {
            return null;
        }

        Map<String, JDruidDataSource> dataSources = new HashMap<>();

        for (ObjectName objectName : abilities.DataSources) {
            JDruidDataSource dataSource = new JDruidDataSource();
            dataSource.ActiveCount = getInt(objectName, "ActiveCount");
            dataSource.PoolingCount = getInt(objectName, "PoolingCount");
            dataSource.MaxActive = getInt(objectName, "MaxActive");
            dataSource.WaitThreadCount = getInt(objectName, "WaitThreadCount");
            dataSources.put(objectName.getKeyProperty("id"), dataSource);
        }

        return dataSources;
    }

    public JTomcat getTomcat() throws Exception {
        if (abilities.Tomcat == null) {
            return null;
        }

        JTomcat tomcat = new JTomcat();
        tomcat.CurrentThreadsBusy = getInt(abilities.Tomcat, "currentThreadsBusy");
        tomcat.CurrentThreadCount = getInt(abilities.Tomcat, "currentThreadCount");
        tomcat.MaxThreads = getInt(abilities.Tomcat, "maxThreads");
        return tomcat;
    }

    private int getInt(ObjectName objectName, String name) throws Exception {
        return (int) mbs.getAttribute(objectName, name);
    }
}
