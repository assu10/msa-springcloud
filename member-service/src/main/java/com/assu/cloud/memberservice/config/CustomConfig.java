package com.assu.cloud.memberservice.config;

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

    public String getYourName() {
        return yourName;
    }

    public String getServiceIdZuul() {
        return serviceIdZuul;
    }
}
