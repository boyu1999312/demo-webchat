package com.test.demoimg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@MapperScan("com.test.demoimg.repo")
@SpringBootApplication
public class DemoImgApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoImgApplication.class, args);
    }

}
