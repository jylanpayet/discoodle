package com.discoodle.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Note {

    private final long user_id;
    private String user_firstName;
    private String user_lastName;
    private final Long group_id;
    private double note;
    private double coef;
    private String titre;

    public Note(long user_id, String user_firstName, String user_lastName, Long group_id, double note, double coef, String titre) {
        this.user_id = user_id;
        this.user_firstName = user_firstName;
        this.user_lastName = user_lastName;
        this.group_id = group_id;
        this.note = note;
        this.coef = coef;
        this.titre = titre;
    }

}
