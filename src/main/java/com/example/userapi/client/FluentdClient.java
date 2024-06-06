package com.example.userapi.client;

import org.fluentd.logger.FluentLogger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FluentdClient {

    private static final String FLUENTD_HOST = "localhost";
    private static final String FLUENTD_PORT = "24224";
    private static final String FLUENTD_TAG = "userapi";

    private final FluentLogger logger = FluentLogger.getLogger(FLUENTD_TAG, FLUENTD_HOST, Integer.parseInt(FLUENTD_PORT));

    public void send(String tag, Map<String, Object> data) {
        logger.log(tag, data);
    }

}