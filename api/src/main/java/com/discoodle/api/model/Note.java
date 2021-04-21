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
}
