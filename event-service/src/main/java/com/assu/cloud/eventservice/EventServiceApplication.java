package com.assu.cloud.eventservice;

import com.assu.cloud.eventservice.config.CustomConfig;
import com.assu.cloud.eventservice.event.model.MemberChangeModel;
import com.assu.cloud.eventservice.utils.CustomContextInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableResourceServer           // 보호 자원으로 설정
@EnableBinding(Sink.class)      // 이 애플리케이션을 메시지 브로커와 바인딩하도록 스프링 클라우드 스트림 설정
                                // Sink.class 로 지정 시 해당 서비스가 Sink 클래스에 정의된 채널들을 이용해 메시지 브로커와 통신
public class EventServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceApplication.class);

    private final CustomConfig customConfig;

    public EventServiceApplication(CustomConfig customConfig) {
        this.customConfig = customConfig;
    }

    /**
     * 채널에서 받은 메시지를 MemberChangeModel 이라는 POJO 로 자동 역직렬화
     * @param mbChange
     */
    @StreamListener(Sink.INPUT)     // 메시지가 입력 채널에서 수신될 때마다 이 메서드 실행
    public void loggerSink(MemberChangeModel mbChange) {
        logger.info("======= Received an event for organization id {}", mbChange.getUserId());
    }

    /**
     * 레디스 서버에 실제 DB 커넥션을 설정
     * 레디스 인스턴스와 통신하려면 JedisConnectionFactory 를 빈으로 노출해야 함
     * 이 커넥션을 사용해서 스프링 RedisTemplate 객체 생성
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(customConfig.getRedisServer());
        jedisConnectionFactory.setPort(customConfig.getRedisPort());
        return jedisConnectionFactory;
    }

    /**
     * 레디스 서버에 작업 수행 시 사용할 RedisTemplate 객체 생성
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

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
