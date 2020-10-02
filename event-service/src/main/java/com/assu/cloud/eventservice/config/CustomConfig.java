package com.assu.cloud.eventservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class CustomConfig {
    @Value("${your.name}")
    private String yourName;

    @Value("${service.id.zuul}")
    private String serviceIdZuul;

    @Value("${signing.key}")
    private String jwtSigningKey;

    @Value("${redis.server}")
    private String redisServer;

    @Value("${redis.port}")
    private int redisPort;

    public String getYourName() {
        return yourName;
    }

    public String getServiceIdZuul() {
        return serviceIdZuul;
    }

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }

    public String getRedisServer() {
        return redisServer;
    }

    public int getRedisPort() {
        return redisPort;
    }
}
