package com.github.mawen12.easeagent.core.plugins.tomcat.metric;

import com.github.mawen12.easeagent.api.metrics.*;
import com.github.mawen12.easeagent.api.utils.ScheduleHelper;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;

public class TomcatMetric extends ServiceMetric implements Runnable {

    private MBeanServer mbs;
    private ObjectName name;

    public TomcatMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public static void buildAndRun() {
        Tags tags = new Tags("application", "tomcat", "resource");
        TomcatMetric metric = ServiceMetricRegistry.getOrCreate(tags, TomcatNameFactory.INSTANCE, TomcatMetric::new);

        metric.init();

        if (metric.name != null) {
            ScheduleHelper.DEFAULT.execute(5, 5, metric);
        }
    }

    public void init() {
        System.out.println("Tomcat Metric init");
        this.mbs = ManagementFactory.getPlatformMBeanServer();

        try {
            Set<ObjectName> names = mbs.queryNames(new ObjectName("Tomcat:type=ThreadPool,*"), null);
            if (names != null && names.size() == 1) {
                this.name = names.iterator().next();
                System.out.println("Tomcat Metric name is " + this.name.getCanonicalName());
            }
        } catch (MalformedObjectNameException e) {
        }
    }

    @Override
    public void run() {
        Map<Metric.SubType, MetricName> map = nameFactory.gaugeNames("tomcat");
        for (Map.Entry<Metric.SubType, MetricName> entry : map.entrySet()) {
            MetricName metricName = entry.getValue();

            try {
                Integer currentThreadsBusy = getInt(name, "currentThreadsBusy");
                Integer currentThreadCount = getInt(name, "currentThreadCount");
                Integer maxThreads = getInt(name, "maxThreads");
                Gauge<GaugeMetricModel.TomcatGaugeMetricModel> gauge = () -> new GaugeMetricModel.TomcatGaugeMetricModel(
                        currentThreadsBusy,
                        currentThreadCount,
                        maxThreads
                );

                metricRegistry.gauge(metricName.getName(), () -> gauge);
            } catch (Exception e) {
                System.err.println("fetch tomcat metric err " + e.getMessage());
            }
        }
    }

    private Integer getInt(ObjectName name, String attribute) throws Exception {
        return (Integer) mbs.getAttribute(name, attribute);
    }

    enum TomcatNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createBuilder()
                    .gauge(DEFAULT, Collections.emptySet())
                    .build();
        }
    }
}
