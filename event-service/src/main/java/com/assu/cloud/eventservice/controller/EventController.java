package com.assu.cloud.eventservice.controller;

import com.assu.cloud.eventservice.client.MemberRestTemplateClient;
import com.assu.cloud.eventservice.client.MemberFeignClient;
import com.assu.cloud.eventservice.config.CustomConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    private final CustomConfig customConfig;
    private final MemberFeignClient memberFeignClient;
    private final MemberRestTemplateClient memberRestTemplateClient;

    public EventController(CustomConfig customConfig, MemberFeignClient memberFeignClient, MemberRestTemplateClient memberRestTemplateClient) {
        this.customConfig = customConfig;
        this.memberFeignClient = memberFeignClient;
        this.memberRestTemplateClient = memberRestTemplateClient;
    }

    @GetMapping(value = "name/{nick}")
    public String getYourName(@PathVariable("nick") String nick) {
        // 히스트릭트 타임아웃을 테스트하기 위함
        /*try {
            Thread.sleep(4000);
        } catch(InterruptedException e) {
            e.printStackTrace();;
        }*/
        return "[EVENT] Your name is " + customConfig.getYourName() + ", nickname is " + nick;
    }

    /**
     * Feign 을 이용하여 회원 서비스 REST API 호출
     */
    @GetMapping(value = "member/{nick}")
    public String getMemberName(@PathVariable("nick") String nick) {
        return memberFeignClient.getYourName(nick);
    }

    /**
     * 회원 서비스에서 호출할 메서드
     */
    @GetMapping(value = "gift/{name}")
    public String gift(@PathVariable("name") String gift) {
        return "[EVENT] Gift is " + gift;
    }

    /*@GetMapping("userInfo/{name}")
    public String userInfo(@PathVariable("name") String name) {
        return "[EVENT-MEMBER] " + memberRestTemplateClient.userInfo(name);
    }*/
}
