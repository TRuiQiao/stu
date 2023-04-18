package com.trq.xtdemo;

import com.trq.xtdemo.biz.test.NumConvertFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
public class Application {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(Application.class, args);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:/data/*");
        System.out.println("获取文件");
        for (Resource resource : resources) {
            System.out.println("文件："+resource.getFilename());
        }
        System.out.println(NumConvertFactory.map.toString());

    }

}
