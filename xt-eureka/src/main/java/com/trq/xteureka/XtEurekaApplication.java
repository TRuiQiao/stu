package com.trq.xteureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class XtEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(XtEurekaApplication.class, args);
    }

}
