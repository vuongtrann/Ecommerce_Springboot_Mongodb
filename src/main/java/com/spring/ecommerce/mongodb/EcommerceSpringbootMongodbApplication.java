package com.spring.ecommerce.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class EcommerceSpringbootMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceSpringbootMongodbApplication.class, args);
    }

}
