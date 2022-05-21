package com.trq.xtdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = "com.trq.xtdemo.biz")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
