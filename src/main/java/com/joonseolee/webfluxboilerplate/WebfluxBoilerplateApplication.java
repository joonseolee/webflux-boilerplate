package com.joonseolee.webfluxboilerplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@EnableConfigurationProperties
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public class WebfluxBoilerplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxBoilerplateApplication.class, args);
    }

}
