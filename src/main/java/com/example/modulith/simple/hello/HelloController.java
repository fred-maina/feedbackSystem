package com.example.modulith.simple.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloController {

    @Value("${hello.text:Hello}")
    private String helloText;

    @GetMapping("/hello")
    String hello() {
        return helloText;
    }
}