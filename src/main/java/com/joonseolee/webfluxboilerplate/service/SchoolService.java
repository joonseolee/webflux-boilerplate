package com.joonseolee.webfluxboilerplate.service;

import com.joonseolee.webfluxboilerplate.entity.School;
import com.joonseolee.webfluxboilerplate.repository.SchoolJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.data.r2dbc.query.Criteria.where;


@RequiredArgsConstructor
@Service
public class SchoolService {

    private final SchoolJpaRepository schoolJpaRepository;
    private final R2dbcEntityOperations mainEntityTemplate;

    public Mono<School> save(Mono<School> school) {
        return school
                .flatMap(schoolJpaRepository::save)
                .switchIfEmpty(Mono.empty());
    }

    /**
     * r2dbc 를 사용한 쿼리
     * @param id
     * @param name
     * @return
     */
    public Mono<School> findByIdAndName(Long id, String name) {
        return mainEntityTemplate.select(School.class)
                .from("school")
                .matching(Query.query(
                        where("id").is(Objects.requireNonNull(id))
                                .and(where("name").is(Objects.requireNonNull(name)))
                )).one();
    }

    public Mono<School> findById(Long id) {
        return schoolJpaRepository.getSchoolById(id);
    }
}
