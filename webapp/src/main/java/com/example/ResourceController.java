package com.example;

//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@EnableResourceServer
public class ResourceController {

    @GetMapping({"/", "/index"})
    public String indexPage() {
        return "index";
    }

}
