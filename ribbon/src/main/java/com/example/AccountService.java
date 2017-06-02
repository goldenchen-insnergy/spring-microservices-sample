package com.example;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class AccountService {

    private static final String SERVER_NAME_OF_EUREKA = "http://account";

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getMessageError")
    public String getMessage() {
        return restTemplate.getForEntity(SERVER_NAME_OF_EUREKA + "/message", String.class).getBody();
    }

    public String getMessageError() {
        return "get message error";
    }

    public String getMessageWithoutCircuitBreaker() {
        return restTemplate.getForEntity(SERVER_NAME_OF_EUREKA + "/message", String.class).getBody();
    }

}