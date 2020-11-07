package com.assu.cloud.memberservice.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BbsRestTemplateClient {

    private final RestTemplate restTemplate;

    public BbsRestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    String ZUUL_URL_PREFIX = "/api/bbs/bbs/";      // bbs 서비스의 주울 라우팅경로와 bbs 클래스 주소

    public String gift(String name) {
        /*ResponseEntity<EventGift> restExchange =
                restTemplate.exchange(
                    "http://event-service/event/gift/{name}",
                    HttpMethod.GET,
                    null, EventGift.class, name
                );*/
        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        "http://zuulserver" + ZUUL_URL_PREFIX + "gift/{name}",   // http://localhost:5555/api/bbs/bbs/gift/flower
                        HttpMethod.GET,
                        null, String.class, name
                );

        return restExchange.getBody();
    }
}
