package com.joonseolee.webfluxboilerplate.service;

import com.joonseolee.webfluxboilerplate.entity.School;
import com.joonseolee.webfluxboilerplate.repository.SchoolJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Service
public class SchoolService {

    private final SchoolJpaRepository schoolJpaRepository;

    public Mono<School> save(Mono<School> school) {
        return school
                .flatMap(schoolJpaRepository::save)
                .switchIfEmpty(Mono.empty());
    }

//    @Transactional(readOnly = false)
    public Mono<School> findById(Mono<Long> id) {
        return id
                .flatMap(schoolJpaRepository::getSchoolById);
    }
}
