package com.discoodle.api.request;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NoteRequest {
    private Long user_id;
    private String userName;
    private Long group_id;
    private double note;
    private double coef;
    private String titre;
    private String note_id;

}
