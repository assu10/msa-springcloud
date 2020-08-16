package com.assu.cloud.eventservice.controller;

import com.assu.cloud.eventservice.config.CustomConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
public class EventController {

    private CustomConfig customConfig;

    public EventController(CustomConfig customConfig) {
        this.customConfig = customConfig;
    }

    @GetMapping(value = "name")
    public String getYourName(String nick) {
        return "Your name is " + customConfig.getYourName() + ", nickname is " + nick;
    }
}
