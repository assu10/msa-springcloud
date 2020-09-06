package com.assu.cloud.memberservice.controller;

import com.assu.cloud.memberservice.client.EventRestTemplateClient;
import com.assu.cloud.memberservice.config.CustomConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping("/member")
public class MemberController {

    private CustomConfig customConfig;
    private EventRestTemplateClient eventRestTemplateClient;

    public MemberController(CustomConfig customConfig, EventRestTemplateClient eventRestTemplateClient) {
        this.customConfig = customConfig;
        this.eventRestTemplateClient = eventRestTemplateClient;
    }

    @GetMapping(value = "name/{nick}")
    public String getYourName(ServletRequest req, @PathVariable("nick") String nick) {
        return "[MEMBER] Your name is " + customConfig.getYourName() + " / nickname is " + nick + " / port is " + req.getServerPort();
    }

    /**
     * RestTemplate 를 이용하여 이벤트 서비스의 REST API 호출
     */
    @GetMapping(value = "gift/{name}")
    public String gift(ServletRequest req, @PathVariable("name") String name) {
        return "[MEMBER] " + eventRestTemplateClient.gift(name) + " / port is " + req.getServerPort();
    }
}
