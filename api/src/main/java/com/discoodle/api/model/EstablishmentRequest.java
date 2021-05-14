package com.discoodle.api.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
@Entity
@Table(name = "establishment_request")
public class EstablishmentRequest {

    public EstablishmentRequest(Long user_id, String name, String description, Groups.TypeOfGroup type, Status status) {
        this.user_id = user_id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.status = status;
    }

    @Id
    @GeneratedValue
    @Column(name = "er_id", unique = true, nullable = false)
    private Long er_id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_group")
    private Groups.TypeOfGroup type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EstablishmentRequest.Status status;

    public enum Status {
        COURS,
        ACCEPTEE,
        REFUSEE
    }
}
