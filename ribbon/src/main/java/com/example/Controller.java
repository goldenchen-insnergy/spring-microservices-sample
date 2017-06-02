package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableResourceServer
class Controller {

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/test")
    public String test() {
        return "ribbon test ok";
    }

    @GetMapping(value = "/message")
    public String getAccountMessage() {
        return accountService.getMessage();
    }

    @GetMapping(value = "/message2")
    public String getAccountMessage2() {
		return accountService.getMessageWithoutCircuitBreaker();
    }

}