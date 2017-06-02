package com.example;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@EnableResourceServer
class Controller {

    @GetMapping("/user") //TODO: remove this, this is for test
    public Principal user(Principal user) {
        return user;
    }

    @GetMapping({"/signin", "/signin/google"}) //TODO: redirect to sign in web view
    public String signinGoogle() {
        return "sign in google";
    }

    @GetMapping({"/signout", "/signout/google"}) //TODO: change to PostMapping
    public String signoutGoogle(HttpServletRequest request) {
        try {
            JSONObject authMain = new JSONObject(SecurityContextHolder.getContext().getAuthentication());
            JSONObject details = authMain.getJSONObject("details");
            String tokenValue = details.getString("tokenValue");

            final String SING_OUT_URL = "https://accounts.google.com/o/oauth2/revoke";
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            HttpEntity<String> entity = new HttpEntity<>("token="+tokenValue, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForEntity(SING_OUT_URL, entity, String.class);
        } catch (Exception ex) {
        } finally {
            HttpSession session = request.getSession(false); //false: 若 session==null 不建立新的
            if (session != null) {
                session.invalidate();
            }
            // 更完整的 context 清除
//			SecurityContextHolder.getContext().setAuthentication(null);
//			SecurityContextHolder.clearContext();
        }
        return "sign out";
    }
}