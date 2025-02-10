package com.pullup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PullUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(PullUpApplication.class, args);
    }

}
