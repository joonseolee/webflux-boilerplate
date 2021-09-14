package com.joonseolee.webfluxboilerplate.repository;

import com.joonseolee.webfluxboilerplate.entity.School;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SchoolJpaRepository extends ReactiveCrudRepository<School, Long> {

    Mono<School> getSchoolById(Long id);
}
