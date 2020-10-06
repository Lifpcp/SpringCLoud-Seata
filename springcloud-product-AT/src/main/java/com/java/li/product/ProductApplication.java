package com.java.li.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan("com.java.li.common")
public class ProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class);
    }
}
