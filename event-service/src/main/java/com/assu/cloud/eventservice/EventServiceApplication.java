package com.assu.cloud.eventservice;

import com.assu.cloud.eventservice.utils.CustomContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.util.Collections;
import java.util.List;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableResourceServer
public class EventServiceApplication {

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
    }

    /*@LoadBalanced       // 스프링 클라우드가 리본이 지원하는 RestTemplate 클래스 생성하도록 지시
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
