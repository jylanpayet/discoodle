package com.discoodle.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Note {

    private Long user_id;
    private String user_firstName;
    private String user_lastName;
    private Long group_id;
    private double note;
    private double coef;
    private String titre;
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
