package com.assu.cloud.memberservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class MemberServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }

    @LoadBalanced       // 스프링 클라우드가 리본이 지원하는 RestTemplate 클래스 생성하도록 지시
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
