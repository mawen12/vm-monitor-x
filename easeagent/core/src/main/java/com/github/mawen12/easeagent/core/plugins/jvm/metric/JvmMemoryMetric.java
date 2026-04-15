package com.github.mawen12.easeagent.core.plugins.jvm.metric;

import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.metrics.*;
import com.github.mawen12.easeagent.api.utils.ScheduleHelper;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;

@EaseAgentClassLoader
public class JvmMemoryMetric extends ServiceMetric implements Runnable {

    private static final Pattern WHITESPACE = Pattern.compile("[\\s]+");

    private JvmMemoryMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public static void buildAndRun() {
        Tags tags = new Tags("app", "jvm-memory", "resource");
        JvmMemoryMetric metric = ServiceMetricRegistry.getOrCreate(tags, JvmMemoryNameFactory.INSTANCE, JvmMemoryMetric::new);

        ScheduleHelper.DEFAULT.execute(10, 10, metric);
    }

    @Override
    public void run() {
        List<MemoryPoolMXBean> beans = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean bean : beans) {
            String name = bean.getName();
            MemoryUsage usage = bean.getUsage();

            String poolName = com.codahale.metrics.MetricRegistry.name("pools", WHITESPACE.matcher(name).replaceAll("-"));

            Map<Metric.SubType, MetricName> map = nameFactory.gaugeNames(poolName);
            for (Map.Entry<Metric.SubType, MetricName> entry : map.entrySet()) {
                MetricName metricName = entry.getValue();

                Gauge<JvmMemoryGaugeMetricModel> gauge = () -> new JvmMemoryGaugeMetricModel(
                        usage.getInit(),
                        usage.getUsed(),
                        usage.getCommitted(),
                        usage.getMax()
                );

                metricRegistry.gauge(metricName.getName(), () -> gauge);
            }
        }
    }

    static class JvmMemoryGaugeMetricModel implements GaugeMetricModel {
        private Long bytesInit;
        private Long bytesUsed;
        private Long bytesCommitted;
        private Long bytesMax;

        public JvmMemoryGaugeMetricModel(Long bytesInit, Long bytesUsed, Long bytesCommitted, Long bytesMax) {
            this.bytesInit = bytesInit;
            this.bytesUsed = bytesUsed;
            this.bytesCommitted = bytesCommitted;
            this.bytesMax = bytesMax;
        }

        @Override
        public Map<String, Object> toHashMap() {
            Map<String, Object> map = new HashMap<>();
            map.put("bytes-init", bytesInit);
            map.put("bytes-used", bytesUsed);
            map.put("bytes-committed", bytesCommitted);
            map.put("bytes-max", bytesMax);
            return map;
        }
    }

    enum JvmMemoryNameFactory implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createBuilder()
                    .gauge(DEFAULT, Collections.emptySet())
                    .build();
        }
    }
}
