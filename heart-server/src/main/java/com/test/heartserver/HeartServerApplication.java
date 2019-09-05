package com.test.heartserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableHystrix
@EnableFeignClients
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class HeartServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeartServerApplication.class, args);
    }

}
