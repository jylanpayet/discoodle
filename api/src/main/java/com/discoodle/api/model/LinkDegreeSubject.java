package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
@IdClass(value=LinkDegreeSubject.KeyDS.class)
@Table(name = "link_degree_subject")
public class LinkDegreeSubject implements Serializable {

    public LinkDegreeSubject(Integer degreeId, Integer subjectId) {
        this.degreeId = degreeId;
        this.subjectId = subjectId;
    }

    @Id
    @Column(name = "degree_id", nullable = false)
    private long degreeId;

    @Id
    @Column(name = "subject_id", nullable = false)
    private long subjectId;

    static class KeyDS implements Serializable{
        private long degreeId;
        private long subjectId;
    }
}
