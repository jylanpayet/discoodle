package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@ToString
@Table(name = "establishment")

public class Establishment {

    public Establishment(String name,Integer directorId,Establishment.Type type) {
        this.name=name;
        this.directorId=directorId;
        this.type=type;
    }

    @Id
    @Column(name = "establishment_id", unique = true, nullable = false)
    private long degreeId;

    @Column(name = "name")
    private String name;

    @Column(name = "director_id")
    private Integer directorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Establishment.Type type;

    public enum Type{
        UNIVERSITY,
        HIGH_SCHOOL,
        MIDDLE_SCHOOL,
        PRIVATE_SCHOOL,
        OTHER
    }
}
