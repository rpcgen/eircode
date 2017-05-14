package com.fexco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@EnableCaching
@SpringBootApplication
public class Application {

    @Configuration
    public static class Config {

        @Bean
        public static RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
