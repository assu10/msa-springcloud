package com.assu.cloud.bbsservice;

import com.assu.cloud.bbsservice.utils.CustomContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker           // Hystrix
public class BbsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BbsServiceApplication.class, args);
    }
    // 기본 RestTemplate
    @LoadBalanced       // 스프링 클라우드가 리본이 지원하는 RestTemplate 클래스 생성하도록 지시
    @Bean
    public RestTemplate getRestTemplate() {
        // return new RestTemplate();
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();

        if (interceptors == null) {
            template.setInterceptors(Collections.singletonList(new CustomContextInterceptor()));
        } else {
            interceptors.add(new CustomContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }
}
