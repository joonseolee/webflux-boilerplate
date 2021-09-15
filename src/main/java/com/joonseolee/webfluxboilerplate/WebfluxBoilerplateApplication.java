package com.joonseolee.webfluxboilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
public class WebfluxBoilerplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxBoilerplateApplication.class, args);
    }

}
