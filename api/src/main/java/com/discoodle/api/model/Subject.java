package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;

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
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "licence_id",nullable = false)
    private long licenceId;

    @Column(name = "name")
    private String name;

    @Column(name = "user_id", unique = true, nullable = false)
    private Integer userId;
}
