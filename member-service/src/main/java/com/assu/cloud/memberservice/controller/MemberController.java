package com.assu.cloud.memberservice.controller;

import com.assu.cloud.memberservice.client.EventRestTemplateClient;
import com.assu.cloud.memberservice.config.CustomConfig;
import com.assu.cloud.memberservice.event.source.SimpleSourceBean;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final CustomConfig customConfig;
    private final EventRestTemplateClient eventRestTemplateClient;
    private final SimpleSourceBean simpleSourceBean;

    public MemberController(CustomConfig customConfig, EventRestTemplateClient eventRestTemplateClient, SimpleSourceBean simpleSourceBean) {
        this.customConfig = customConfig;
        this.eventRestTemplateClient = eventRestTemplateClient;
        this.simpleSourceBean = simpleSourceBean;
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

    /**
     * ADMIN 권한 소유자만 PUT METHOD API 호출 가능하도록 설정 후 테스트
     */
    @PutMapping("{name}")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public String member(@PathVariable("name") String name) {
        return "[MEMBER-DELETE] " + name + " is deleted.";
    }

    /**
     * 이벤트 서비스에서 OAuth2 로 호출 테스트
     */
    @GetMapping("userInfo/{name}")
    public String userInfo(@PathVariable("name") String name) {
        return "[MEMBER] " + name;
    }

    /**
     * 단순 메시지 발행
     */
    @PostMapping("/{userId}")
    public void saveUserId(@PathVariable("userId") String userId) {
        // DB 에 save 작업..
        simpleSourceBean.publishMemberChange("SAVE", userId);
    }

}
