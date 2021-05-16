package com.discoodle.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "note")
public class Note {

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "group_id")
    private Long group_id;

    @Column(name = "note")
    private double note;

    @Column(name = "coef")
    private double coef;

    @Column(name = "titre")
    private String titre;

    @Id
    @GeneratedValue
    @Column(name = "note_id", unique = true, nullable = false)
    private Long note_id;

    public Note(Long user_id, Long group_id, double note, double coef, String titre) {
        this.user_id = user_id;
        this.group_id = group_id;
        this.note = note;
        this.coef = coef;
        this.titre = titre;

    }
}
