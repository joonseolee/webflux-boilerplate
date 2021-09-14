package com.joonseolee.webfluxboilerplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;

@Configuration
public class ServerConfiguration {

//    @Bean
//    public RouterFunction<ServerResponse> routes() {
//        return RouterFunctions.route(RequestPredicates.GET("/"),
//                req -> ServerResponse.ok().body(Flux.just("Hello", "World!"), String.class));
//    }
}
