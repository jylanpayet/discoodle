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
@IdClass(value=LinkUserSubject.KeyUS.class)
@Table(name = "link_user_subject")
public class LinkUserSubject implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Id
    @Column(name = "subject_id", nullable = false)
    private long subjectId;

    static class KeyUS implements Serializable{

        private long userId;
        private long subjectId;
    }
}
