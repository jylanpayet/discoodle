package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@ToString
@Table(name = "degree")

public class Degree {

    public Degree(String name, Integer year) {
        this.name = name;
        this.year = year;
    }
    @Id
    @SequenceGenerator(
            name = "degree_sequence",
            sequenceName = "degree_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "degree_sequence"
    )
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private Integer year;
}
