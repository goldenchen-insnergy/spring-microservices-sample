package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@EnableOAuth2Sso
@SpringCloudApplication
public class ZuulApplication {

//	@Bean
//	public DomainFilter domainFilter() {
//		return new DomainFilter();
//	}

	public static void main(String[] args) {
		SpringApplication.run(ZuulApplication.class, args);
	}
}



