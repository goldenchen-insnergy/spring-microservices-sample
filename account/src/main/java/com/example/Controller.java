package com.example;

import com.google.gdata.util.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RefreshScope
@RestController
public class Controller {

    @Value("${message: Default msg}")
    private String message;

    @GetMapping("/message")
    public String message() throws GeneralSecurityException, IOException, ServiceException {
        return this.message;
    }

    @GetMapping("/users")
    public String getDomainUsers() throws Exception {
        GoogleDirectoryResource.getDomainUsers();
        return "getDomainUsers";
    }
}
