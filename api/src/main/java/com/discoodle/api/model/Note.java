package com.discoodle.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Note {

    private Long user_id;
    private String userName;
    private Long group_id;
    private double note;
    private double coef;
    private String titre;
    private String note_id;

    public Note(Long user_id, String userName, Long group_id, double note, double coef, String titre, String note_id) {
        this.user_id = user_id;
        this.userName = userName;
        this.group_id = group_id;
        this.note = note;
        this.coef = coef;
        this.titre = titre;
        this.note_id = note_id;
    }


}
