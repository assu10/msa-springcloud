package com.assu.cloud.memberservice.client;

import com.assu.cloud.memberservice.config.CustomConfig;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EventRestTemplateClient {

    private final RestTemplate restTemplate;
    private final CustomConfig customConfig;

    public EventRestTemplateClient(RestTemplate restTemplate, CustomConfig customConfig) {
        this.restTemplate = restTemplate;
        this.customConfig = customConfig;
    }

    String ZUUL_URL_PREFIX = "/api/evt/event/";      // 이벤트 서비스의 주울 라우팅경로와 이벤트 클래스 주소

    public String gift(String name) {
        System.out.println("----------http://" + customConfig.getServiceIdZuul() + ZUUL_URL_PREFIX + "gift/{name}");
        /*ResponseEntity<EventGift> restExchange =
                restTemplate.exchange(
                    "http://event-service/event/gift/{name}",
                    HttpMethod.GET,
                    null, EventGift.class, name
                );*/
        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        "http://" + customConfig.getServiceIdZuul() + ZUUL_URL_PREFIX + "gift/{name}",   // http://localhost:5555/api/mb/member/gift/flower
                        HttpMethod.GET,
                        null, String.class, name
                );

        return restExchange.getBody();
    }

    public String gift2(String name) {
        /*ResponseEntity<EventGift> restExchange =
                restTemplate.exchange(
                    "http://event-service/event/gift/{name}",
                    HttpMethod.GET,
                    null, EventGift.class, name
                );*/
        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        "http://" + customConfig.getServiceIdZuul() + ZUUL_URL_PREFIX + "gift2/{name}",   // http://localhost:5555/api/mb/member/gift/flower
                        HttpMethod.GET,
                        null, String.class, name
                );

        return restExchange.getBody();
    }
}
