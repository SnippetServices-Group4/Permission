package com.services.group4.permission.controller;

import org.springframework.web.bind.annotation.*;

@RestController("/test")
public class TestController {

    @GetMapping("/ping")
    public String index() {
        return "pong";
    }
}
