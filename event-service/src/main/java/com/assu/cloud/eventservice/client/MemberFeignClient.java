package com.assu.cloud.eventservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="member-service",url = "http://localhost:8090/member/")
@FeignClient("${member.service.id}")
public interface MemberFeignClient {
    @GetMapping(value = "member/name/{nick}")
    String getYourName(@PathVariable("nick") String nick);
}
