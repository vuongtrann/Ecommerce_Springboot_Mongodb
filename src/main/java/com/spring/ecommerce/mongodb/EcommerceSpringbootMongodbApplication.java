package com.spring.ecommerce.mongodb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@SpringBootApplication
@EnableMongoRepositories
public class EcommerceSpringbootMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceSpringbootMongodbApplication.class, args);
    }

}
