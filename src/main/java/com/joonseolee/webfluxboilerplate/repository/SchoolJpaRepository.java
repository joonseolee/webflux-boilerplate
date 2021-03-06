package com.joonseolee.webfluxboilerplate.repository;

import com.joonseolee.webfluxboilerplate.entity.School;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SchoolJpaRepository extends ReactiveCrudRepository<School, Long> {

    Mono<School> getSchoolById(Long id);
}
