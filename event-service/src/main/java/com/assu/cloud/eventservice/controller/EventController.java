package com.assu.cloud.eventservice.controller;

import com.assu.cloud.eventservice.client.MemberCacheRestTemplateClient;
import com.assu.cloud.eventservice.client.MemberRestTemplateClient;
import com.assu.cloud.eventservice.client.MemberFeignClient;
import com.assu.cloud.eventservice.config.CustomConfig;
import com.assu.cloud.eventservice.model.Member;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping("/event")
public class EventController {

    private final CustomConfig customConfig;
    private final MemberFeignClient memberFeignClient;
    private final MemberRestTemplateClient memberRestTemplateClient;
    private final MemberCacheRestTemplateClient memberCacheRestTemplateClient;

    public EventController(CustomConfig customConfig, MemberFeignClient memberFeignClient, MemberRestTemplateClient memberRestTemplateClient,
                           MemberCacheRestTemplateClient memberCacheRestTemplateClient) {
        this.customConfig = customConfig;
        this.memberFeignClient = memberFeignClient;
        this.memberRestTemplateClient = memberRestTemplateClient;
        this.memberCacheRestTemplateClient = memberCacheRestTemplateClient;
    }

    @GetMapping(value = "name/{nick}")
    public String getYourName(ServletRequest req, @PathVariable("nick") String nick) {
        // 히스트릭트 타임아웃을 테스트하기 위함
        /*try {
            Thread.sleep(4000);
        } catch(InterruptedException e) {
            e.printStackTrace();;
        }*/
        return "[EVENT] Your name is " + customConfig.getYourName() + ", nickname is " + nick + ", port is " + req.getServerPort();
    }

    /**
     * Feign 을 이용하여 회원 서비스 REST API 호출
     */
    @GetMapping(value = "member/{nick}")
    public String getMemberName(@PathVariable("nick") String nick) {
        return memberFeignClient.getYourName(nick);
    }

    /**
     * 회원 서비스에서 호출할 메서드 (fallback test)
     */
    @GetMapping(value = "gift/{name}")
    public String gift(@PathVariable("name") String gift) {
        //sleep();
        return "[EVENT] Gift is " + gift;
    }

    /**
     * 회원 서비스에서 호출할 메서드 (fallback test)
     */
    @GetMapping(value = "gift2/{name}")
    public String gift2(@PathVariable("name") String gift) {
        return "[EVENT] Gift is " + gift;
    }


    @GetMapping(value = "timeout")
    public String timeout() {

        sleep();
        return "[EVENT] good";
    }

    private void sleep() {
        try {
            Thread.sleep(10000);        // 7,000 ms (7초), 기본적으로 히스트릭스는 1초 후에 호출을 타임아웃함
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 레디스 캐싱 데이터 사용
     */
    @GetMapping(value = "{userId}")
    public Member userInfo(@PathVariable("userId") String userId) {
        return memberCacheRestTemplateClient.getMember(userId);
    }

    /*@GetMapping("userInfo/{name}")
    public String userInfo(@PathVariable("name") String name) {
        return "[EVENT-MEMBER] " + memberRestTemplateClient.userInfo(name);
    }*/
}
