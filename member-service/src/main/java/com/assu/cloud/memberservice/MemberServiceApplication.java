package com.assu.cloud.memberservice;

import com.assu.cloud.memberservice.utils.CustomContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@EnableResourceServer       // 보호 자원으로 설정
public class MemberServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }

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
