package com.assu.cloud.eventservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="member-service",url = "http://localhost:8090/member/")
//@FeignClient("${service.id.member}")
@FeignClient("${service.id.zuul}")        // OK
public interface MemberFeignClient {
    
    String URL_PREFIX = "/api/mb/member/";      // 회원 서비스의 주울 라우팅경로와 회원 클래스 주소

    /**
     * 주울을 통해 호출할 경로 : http://localhost:5555/api/evt/event/member/{nick}
     */
    @GetMapping(value = URL_PREFIX + "name/{nick}")
    String getYourName(@PathVariable("nick") String nick);
}
