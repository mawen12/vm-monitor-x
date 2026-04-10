package com.github.mawen12.easeagent.core.plugins.jvm.metric;

import com.github.mawen12.easeagent.api.metrics.*;
import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

import static com.github.mawen12.easeagent.api.metrics.Metric.Field.*;
import static com.github.mawen12.easeagent.api.metrics.Metric.FieldWrapper.of;
import static com.github.mawen12.easeagent.api.metrics.Metric.SubType.DEFAULT;
import static com.github.mawen12.easeagent.api.metrics.Metric.ValueFetcher.*;

public class JvmGCMetric extends ServiceMetric {
    private static final String NO_GC = "No GC";

    private JvmGCMetric(MetricRegistry metricRegistry, NameFactory nameFactory) {
        super(metricRegistry, nameFactory);
    }

    public static void buildAndRun() {
        Tags tags = new Tags("application", "jvm-gc", "resource");

        JvmGCMetric metric = ServiceMetricRegistry.getOrCreate(tags, NameFactorySupplier.INSTANCE, JvmGCMetric::new);
        metric.run();
    }

    public void run() {
        for (GarbageCollectorMXBean bean : ManagementFactory.getGarbageCollectorMXBeans()) {
            if (!(bean instanceof NotificationEmitter)) {
                continue;
            }

            NotificationEmitter emitter = (NotificationEmitter) bean;
            GCNotificationListener listener = new GCNotificationListener();
            emitter.addNotificationListener(listener, null, null);
        }
    }

    enum NameFactorySupplier implements NameFactory.Supplier {
        INSTANCE;

        @Override
        public NameFactory nameFactory() {
            return NameFactory.createBuilder()
                    .meter(DEFAULT,
                            of(TIMES, MeterCount),
                            of(TIMES_RATE, MeterMeanRate)
                    )
                    .counter(DEFAULT,
                            of(TOTAL_COLLECTION_TIME, CountingCount)
                    )
                    .build();
        }
    }

    class GCNotificationListener implements NotificationListener {

        @Override
        public void handleNotification(Notification notification, Object handback) {
            if (!notification.getType().equals(com.sun.management.GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                return;
            }

            CompositeData data = (CompositeData) notification.getUserData();
            GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from(data);
            String gcCause = info.getGcCause();
            GcInfo gcInfo = info.getGcInfo();
            long duration = gcInfo.getDuration();

            String gcName = info.getGcName();

            nameFactory.meterNames(gcName)
                    .forEach((type, name) -> {
                        Meter meter = metricRegistry.meter(name.getName());
                        if (!NO_GC.equals(gcCause)) {
                            meter.mark();
                        }
                    });

            nameFactory.counterNames(gcName)
                    .forEach((type, name) -> {
                        Counter counter = metricRegistry.counter(name.getName());
                        if (!NO_GC.equals(gcCause)) {
                            counter.inc(duration);
                        }
                    });
        }
    }
}
