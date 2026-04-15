package com.github.mawen12.easeagent.core.metrics;

import com.github.mawen12.easeagent.api.Agent;
import com.github.mawen12.easeagent.api.annotation.EaseAgentClassLoader;
import com.github.mawen12.easeagent.api.logging.Logger;
import fi.iki.elonen.NanoHTTPD;
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Enumeration;

@EaseAgentClassLoader
public class MetricServer extends NanoHTTPD {
    private static final Logger LOGGER = Agent.getLogger(MetricServer.class);

    public MetricServer(int port) {
        super(port);
    }

    public void start() throws IOException {
        super.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        LOGGER.info("start agent metric server on port:{}", getListeningPort());
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (!"/metrics".equals(session.getUri())) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "Not Found");
        }

        try {
            Enumeration<Collector.MetricFamilySamples> samples = CollectorRegistry.defaultRegistry.filteredMetricFamilySamples(Collections.emptySet());
            StringWriter writer = new StringWriter();
            TextFormat.write004(writer, samples);

            return newFixedLengthResponse(Response.Status.OK, TextFormat.CONTENT_TYPE_004, writer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", e.getMessage());
        }
    }
}
