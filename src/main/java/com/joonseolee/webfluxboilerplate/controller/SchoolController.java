package com.joonseolee.webfluxboilerplate.controller;

import com.joonseolee.webfluxboilerplate.entity.School;
import com.joonseolee.webfluxboilerplate.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping("/schools/{id}")
    public Mono<School> findSchoolById(@PathVariable("id") Long id) {
        return schoolService.findById(id);
    }

    @PostMapping("/schools")
    public Mono<School> saveSchool(@RequestBody Mono<School> school) {
        return schoolService.save(school);
    }
}
