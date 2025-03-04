package com.mecook.mecookbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MeCookApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeCookApplication.class, args);
    }

}
