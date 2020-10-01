package com.assu.cloud.eventservice.client;

import com.assu.cloud.eventservice.config.CustomConfig;
import com.assu.cloud.eventservice.utils.CustomContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

@Component
public class MemberRestTemplateClient {

    /*private final OAuth2RestTemplate restTemplate;
    private final CustomConfig customConfig;

    public MemberRestTemplateClient(OAuth2RestTemplate restTemplate, CustomConfig customConfig) {
        this.restTemplate = restTemplate;
        this.customConfig = customConfig;
    }

    String URL_PREFIX = "/api/mb/member/";      // 회원 서비스의 주울 라우팅경로와 회원 클래스 주소

    private static final Logger logger = LoggerFactory.getLogger(MemberRestTemplateClient.class);

    public String userInfo(String name) {
        logger.debug("===== In Member Service.userInfo: {}", CustomContext.getCorrelationId());

        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        //"http://" + customConfig.getServiceIdZuul() + URL_PREFIX + "userInfo/{name}",   // http://localhost:5555/api/mb/member/userInfo/rinda
                        "http://localhost:5555/api/mb/member/userInfo/{name}",   // http://localhost:5555/api/mb/member/userInfo/rinda
                        HttpMethod.GET,
                        null, String.class, name
                );

        return restExchange.getBody();
    }*/
}
