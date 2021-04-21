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


    @Id
    @GeneratedValue
    @Column(name = "user_id", unique = true, nullable = false)
    private Long user_id;

    @Column(name = "user_fristName")
    private String user_firstName;

    @Column(name = "user_lastName")
    private String user_lastName;

    @Column(name = "group_id")
    private Long group_id;

    @Column(name = "note")
    private double note;

    @Column(name = "coef")
    private double coef;

    @Column(name = "titre")
    private String titre;

    @Column(name = "note_id")
    private Long note_id;

    public Note(Long user_id, String user_firstName, String user_lastName, Long group_id, double note, double coef, String titre, Long note_id) {
        this.user_id = user_id;
        this.user_firstName = user_firstName;
        this.user_lastName = user_lastName;
        this.group_id = group_id;
        this.note = note;
        this.coef = coef;
        this.titre = titre;
        this.note_id = note_id;
    }
}
