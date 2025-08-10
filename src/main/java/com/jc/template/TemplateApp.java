package com.jc.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TemplateApp {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApp.class, args);
    }

}
