package com.joonseolee.webfluxboilerplate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class BaseController {

    @GetMapping("/hello")
    public Flux<String> hello() {
        return Flux.just("hello", "world");
    }

    @GetMapping("endless")
    public Flux<Map<String, Integer>> stream() {
        Stream<Integer> stream = Stream.iterate(0, i -> i + 1);
        var value = Flux.never()
                .defaultIfEmpty("DEFAULT")
                .doOnNext(System.out::println)
                .map(v -> v + "2");
        value.subscribe();
        return Flux.fromStream(stream)
                .zipWith(Flux.interval(Duration.ofSeconds(1)))
                .map(tuple -> Collections.singletonMap("value", tuple.getT1()));
    }

    @PostMapping("/echo")
    Flux<Object> echo(@RequestBody Flux<Map<String, Integer>> body) {
        return body.map(value -> {
            Map<String, Integer> map = new HashMap<>();
            map.put("double", value.get("value"));
            return map;
        });
    }
}