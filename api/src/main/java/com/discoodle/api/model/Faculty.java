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
@Table(name = "faculty")

public class Faculty {

    public Faculty(String mention, Integer directorId  ) {
        this.mention= mention;
        this.directorId = directorId;

    }
    @Id
    @Column(name = "faculty_id", unique = true, nullable = false)
    private long falcuty_Id;

    @Column(name = "mention")
    private String mention;

    @Column(name = "director_id")
    private Integer directorId;

    @ManyToMany
    @JoinTable( name = "link_faculty_level",
            joinColumns = @JoinColumn( name = "falcuty_id" ),
            inverseJoinColumns = @JoinColumn( name = "level_id" ) )
    private List<Level> level = new ArrayList<>();

}
