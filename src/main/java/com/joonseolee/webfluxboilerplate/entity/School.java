package com.joonseolee.webfluxboilerplate.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table
public class School {

    private Long id;

    private String name;
}
