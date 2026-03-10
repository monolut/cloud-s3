package com.clouds3.objectservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ObjectServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ObjectServiceApplication.class, args);
    }

}
