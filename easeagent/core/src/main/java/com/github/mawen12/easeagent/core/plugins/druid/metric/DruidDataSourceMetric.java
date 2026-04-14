package com.github.mawen12.easeagent.core.plugins.druid.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.*;
import com.github.mawen12.easeagent.api.utils.ScheduleHelper;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;

@EaseAgentClassLoader
public class DruidDataSourceMetric extends ServiceMetric implements Runnable {

    private MBeanServer mbs;
    private Set<ObjectName> names;

    public DruidDataSourceMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public static void buildAndRun() {
        Tags tags = new Tags("application", "druid-pool", "resource");
        DruidDataSourceMetric metric = ServiceMetricRegistry.getOrCreate(tags, DruidNameFactory.INSTANCE, DruidDataSourceMetric::new);
        metric.init();

        if (metric.names != null && !metric.names.isEmpty()) {
            ScheduleHelper.DEFAULT.execute(5, 5, metric);
        }
    }

    private void init() {
        this.mbs = ManagementFactory.getPlatformMBeanServer();
        try {
            Set<ObjectName> names = mbs.queryNames(new ObjectName("com.alibaba.druid:type=DruidDataSource,*"), null);
            this.names = names;
            System.out.println("Druid Metric name is " + Objects.toString(this.names));
        } catch (MalformedObjectNameException e) {
            System.err.println("Druid Metric init failed " + e.getMessage());
        }
    }

    @Override
    public void run() {
        for (ObjectName name : names) {
            String datasourceName = com.codahale.metrics.MetricRegistry.name("datasources", name.getCanonicalName());
            Map<Metric.SubType, MetricName> map = nameFactory.gaugeNames(datasourceName);
            for (Map.Entry<Metric.SubType, MetricName> entry : map.entrySet()) {
                MetricName metricName = entry.getValue();

                try {
                    Integer activeCount = getInt(name, "ActiveCount");
                    Integer poolingCount = getInt(name, "PoolingCount");
                    Integer maxActive = getInt(name, "MaxActive");
                    Integer waitThreadCount = getInt(name, "WaitThreadCount");
                    Gauge<DruidDataSourceGaugeMetricModel> gauge = () -> new DruidDataSourceGaugeMetricModel(
                            activeCount,
                            poolingCount,
                            maxActive,
                            waitThreadCount
                    );

                    metricRegistry.gauge(metricName.getName(), () -> gauge);
                } catch (Exception e) {
                    System.err.println("Druid metric fetch failed " + e.getMessage());
                }
            }
        }
    }

    private Integer getInt(ObjectName name, String attribute) throws Exception {
        return (Integer) mbs.getAttribute(name, attribute);
    }

    enum DruidNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createBuilder()
                    .gauge(DEFAULT, Collections.emptySet())
                    .build();
        }
    }
}
