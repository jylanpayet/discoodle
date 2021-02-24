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
@Table(name = "subject")
public class Subject {

    @Id
    @SequenceGenerator(
            name = "subject_sequence",
            sequenceName = "subject_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subject_sequence"
    )
    @Column(name = "subject_id", unique = true, nullable = false)
    private long id;

    @Column(name = "name")
    private String name;

    //User Teacher director
    @Column(name = "user_id", unique = true, nullable = false)
    private Integer userId;

    @ManyToMany
    @JoinTable( name = "link_degree_subject",
            joinColumns= @JoinColumn( name = "subject_id" ),
            inverseJoinColumns = @JoinColumn( name = "degree_id" )
            )
    private List<Degree> degrees = new ArrayList<>();

    @ManyToMany
    @JoinTable( name = "link_user_subject",
            joinColumns= @JoinColumn( name = "subject_id" ),
            inverseJoinColumns = @JoinColumn( name = "user_id" )
    )
    private List<User> users = new ArrayList<>();
}
