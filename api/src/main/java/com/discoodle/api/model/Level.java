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
@Table(name = "level")

public class Level {

    public Level(String year ) {
        this.year = year;
    }

    @Id
    @Column(name = "level_id", unique = true, nullable = false)
    private long level_Id;

    @Column(name = "year")
    private String year;

    @ManyToMany
    @JoinTable( name = "link_faculty_level",
            joinColumns = @JoinColumn( name = "level_id" ),
            inverseJoinColumns = @JoinColumn( name = "faculty_id" ) )
    private List<Faculty> faculty = new ArrayList<>();

    @ManyToMany
    @JoinTable( name = "link_level_subject",
            joinColumns = @JoinColumn( name = "level_id" ),
            inverseJoinColumns = @JoinColumn( name = "subject_id" ) )
    private List<Subject> subjects = new ArrayList<>();

}

