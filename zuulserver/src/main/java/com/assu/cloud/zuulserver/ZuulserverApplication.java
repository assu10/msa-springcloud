package com.assu.cloud.zuulserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableZuulProxy        // 주울 서버로 사용
//@EnableCircuitBreaker
public class ZuulserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulserverApplication.class, args);
    }
}
