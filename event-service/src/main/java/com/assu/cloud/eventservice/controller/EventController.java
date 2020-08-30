package com.assu.cloud.eventservice.controller;

import com.assu.cloud.eventservice.client.MemberFeignClient;
import com.assu.cloud.eventservice.config.CustomConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    private CustomConfig customConfig;
    private MemberFeignClient memberFeignClient;

    public EventController(CustomConfig customConfig, MemberFeignClient memberFeignClient) {
        this.customConfig = customConfig;
        this.memberFeignClient = memberFeignClient;
    }

    @GetMapping(value = "name/{nick}")
    public String getYourName(@PathVariable("nick") String nick) {
        try {
            Thread.sleep(4000);
        } catch(InterruptedException e) {
            e.printStackTrace();;
        }
        return "[EVENT] Your name is " + customConfig.getYourName() + ", nickname is " + nick;
    }

    /**
     * 회원 서비스 REST API 호출
     */
    @GetMapping(value = "member/{nick}")
    public String getMemberName(@PathVariable("nick") String nick) {
        return memberFeignClient.getYourName(nick);
    }
}
