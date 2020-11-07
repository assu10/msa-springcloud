package com.assu.cloud.bbsservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping("/bbs")
public class BbsController {
    private static final Logger logger = LoggerFactory.getLogger(BbsController.class);

    @GetMapping(value = "gift/{name}")
    public String gift(@PathVariable("name") String gift) {
        return "[BBS] Gift is " + gift;
    }
}
