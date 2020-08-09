package com.assu.cloud.memberservice.controller;

import com.assu.cloud.memberservice.config.CustomConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private CustomConfig customConfig;

    public MemberController(CustomConfig customConfig) {
        this.customConfig = customConfig;
    }

    @GetMapping(value = "name")
    public String getYourName(String nick) {
        return "Your name is " + customConfig.getYourName() + ", nickname is " + nick;
    }
}
