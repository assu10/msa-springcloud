package com.assu.cloud.eventservice;

import com.assu.cloud.eventservice.utils.CustomContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableResourceServer       // 보호 자원으로 설정
public class EventServiceApplication {

    /**
     * 사용자 정의 RestTemplate 빈을 생성하여 토큰 삽입
     * RestTemplate 기반 호출이 수행되기 전 후킹되는 메서드
     */
    @Primary
    @LoadBalanced
    @Bean
    public RestTemplate getCustomRestTemplate() {
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();

        // CustomContextInterceptor 는 Authorization 헤더를 모든 REST 호출에 삽입함
        if (interceptors == null) {
            template.setInterceptors(Collections.singletonList(new CustomContextInterceptor()));
        } else {
            interceptors.add(new CustomContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }

    /*
    // OAuth2 RestTemplate -> JWT 기반 토큰을 전파하지 앟음
    //@LoadBalanced
    @Bean
    public OAuth2RestTemplate restTemplate(UserInfoRestTemplateFactory factory) {
        List interceptors = factory.getUserInfoRestTemplate().getInterceptors();

        if (interceptors == null) {
            factory.getUserInfoRestTemplate().setInterceptors(Collections.singletonList(new CustomContextInterceptor()));
        } else {
            interceptors.add(new CustomContextInterceptor());
            factory.getUserInfoRestTemplate().setInterceptors(interceptors);
        }
        return factory.getUserInfoRestTemplate();
    }*/

    /*
    기본 RestTemplate
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
    }*/

    public static void main(String[] args) {
        SpringApplication.run(EventServiceApplication.class, args);
    }
}
