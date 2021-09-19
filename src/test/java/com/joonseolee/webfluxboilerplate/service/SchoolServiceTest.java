package com.joonseolee.webfluxboilerplate.service;

import com.joonseolee.webfluxboilerplate.entity.School;
import com.joonseolee.webfluxboilerplate.repository.SchoolJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    @InjectMocks
    private SchoolService schoolService;

    @Mock
    private SchoolJpaRepository schoolJpaRepository;

    @Test
    void whenSaveValidValue_thenSuccess() {
        School school = new School();
        school.setName("new school");
        School savedSchool = new School(1L, "new school");
        when(schoolJpaRepository.save(school)).thenReturn(Mono.just(savedSchool));

        Mono<School> result = schoolService.save(Mono.just(school));

        assertThat(result.block(), notNullValue());
        assertThat(Objects.requireNonNull(result.block()).getId(), is(savedSchool.getId()));
        assertThat(Objects.requireNonNull(result.block()).getName(), is(school.getName()));
    }

    @Test
    void whenGivenValidId_thenSuccess() {
        long id = 23;
        School school = new School(id, "young");
        when(schoolJpaRepository.getSchoolById(id)).thenReturn(Mono.just(school));

        Mono<School> result = schoolService.findById(id);

        assertThat(result.block(), notNullValue());
        assertThat(Objects.requireNonNull(result.block()).getId(), is(id));
        assertThat(Objects.requireNonNull(result.block()).getName(), is("young"));
    }
}