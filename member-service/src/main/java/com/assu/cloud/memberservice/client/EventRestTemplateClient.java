package com.assu.cloud.memberservice.client;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EventRestTemplateClient {

    RestTemplate restTemplate;

    public EventRestTemplateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String gift(String name) {
        /*ResponseEntity<EventGift> restExchange =
                restTemplate.exchange(
                    "http://event-service/event/gift/{name}",
                    HttpMethod.GET,
                    null, EventGift.class, name
                );*/
        ResponseEntity<String> restExchange =
                restTemplate.exchange(
                        "http://event-service/event/gift/{name}",
                        HttpMethod.GET,
                        null, String.class, name
                );

        return restExchange.getBody();
    }
}
