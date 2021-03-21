package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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
    @Column(name = "degree_id", unique = true, nullable = false)
    private long degreeId;

    @Column(name = "name")
    private String name;

    @Column(name = "year")
    private Integer year;

    @ManyToMany
    @JoinTable( name = "link_degree_subject",
            joinColumns = @JoinColumn( name = "degree_id" ),
            inverseJoinColumns = @JoinColumn( name = "subject_id" ) )
    private List<Subject> subjects = new ArrayList<>();

}
