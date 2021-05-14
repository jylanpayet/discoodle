package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
@Table(name = "teacher_request")
public class TeacherRequest {

    public TeacherRequest(Long user_id, TeacherRequest.Status status){
        this.user_id = user_id;
        this.status = status;
    }

    @Id
    @GeneratedValue
    @Column(name = "tr_id", unique = true, nullable = false)
    private Long tr_id;

    @Column(name = "user_id")
    private Long user_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    public enum Status {
        ACCEPTEE,
        REFUSEE,
        COURS
    }
}
