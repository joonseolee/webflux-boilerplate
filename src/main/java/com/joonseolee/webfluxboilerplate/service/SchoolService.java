package com.joonseolee.webfluxboilerplate.service;

import com.joonseolee.webfluxboilerplate.entity.School;
import com.joonseolee.webfluxboilerplate.repository.SchoolJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


//@RequiredArgsConstructor
@Service
public class SchoolService {

    @Autowired
    private SchoolJpaRepository schoolJpaRepository;

    public Mono<School> save(Mono<School> school) {
        return school
                .flatMap(schoolJpaRepository::save)
                .switchIfEmpty(Mono.empty());
    }

    public Mono<School> findById(Mono<Long> id) {
        return id
                .flatMap(schoolJpaRepository::getSchoolById);
    }
}
