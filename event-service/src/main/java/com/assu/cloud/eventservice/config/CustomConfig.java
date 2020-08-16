package com.assu.cloud.eventservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class CustomConfig {
    @Value("${your.name}")
    private String yourName;

    public String getYourName() {
        return yourName;
    }
}
