package com.clouds3.objectservice.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {

            var attributes =
                    (org.springframework.web.context.request.ServletRequestAttributes)
                            org.springframework.web.context.request.RequestContextHolder.getRequestAttributes();

            if (attributes == null) return;

            var request = attributes.getRequest();

            String authorization = request.getHeader("Authorization");

            if (authorization != null) {
                requestTemplate.header("Authorization", authorization);
            }
        };
    }
}
