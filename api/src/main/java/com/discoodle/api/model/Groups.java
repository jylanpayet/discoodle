package com.discoodle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "groups")
public class Groups {

    public Groups(Long parent_id, Integer depth, String name, String description, TypeOfGroup type, String token) {
        this.parent_id = parent_id;
        this.depth = depth;
        this.name = name;
        this.description = description;
        this.type = type;
        this.token = token;
    }

    @Id
    @GeneratedValue
    @Column(name = "groups_id", unique = true, nullable = false)
    private Long groups_id;

    @Column(name = "parent_id")
    private Long parent_id;

    @Column(name = "depth")
    private Integer depth;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "token")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_group")
    private TypeOfGroup type;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "link_groups_to_group",
            joinColumns = @JoinColumn(name = "groups_id"),
            inverseJoinColumns = @JoinColumn(name = "son_id", referencedColumnName = "groups_id"))
    private List<Groups> underGroups = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "link_groups_to_user",
            joinColumns = @JoinColumn(name = "groups_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @JsonIgnore
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinTable(name = "link_groups_to_server",
            joinColumns = @JoinColumn(name = "groups_id"),
            inverseJoinColumns = @JoinColumn(name = "server_id"))
    private Server server;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "link_group_to_roles",
            joinColumns = @JoinColumn(name = "groups_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> roles = new ArrayList<>();


    public enum TypeOfGroup {
        DISCOODLE,           // Admins de discoodle
        ESTABLISHMENT,       // Établissement (si ce n'est pas une université)
        FACULTY,             // Université
        ADMINISTRATION,      // Administration d'un établissement
        COURSE,              // Filière (ex: informatique, histoire, ...)
        GRADE,               // Année (ex: L2, M1, 2nde, ...)
        SUBJECTS,            // Matière enseignée (ex: AAL, Math, etc)
        OTHER,               // Sous groupe annexe, ne sera pas affiché dans les groupes
    }
}
