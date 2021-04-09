package com.discoodle.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.annotation.processing.Generated;
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

    public Groups(Long parent_id, Integer depth, String name,String description, TypeOfGroup type, String key) {
        this.parent_id=parent_id;
        this.depth = depth;
        this.name = name;
        this.description=description;
        this.type = type;
        this.key = key;
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

    @Column(name = "key")
    private String key;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_of_group")
    private TypeOfGroup type;

    @JsonIgnore
    @OneToMany
    @JoinTable( name = "link_groups_to_group",
            joinColumns = @JoinColumn( name = "groups_id"),
            inverseJoinColumns = @JoinColumn( name = "son_id", referencedColumnName = "groups_id"))
    private List<Groups> underGroups = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable( name = "link_groups_to_user",
            joinColumns = @JoinColumn( name = "groups_id" ),
            inverseJoinColumns = @JoinColumn( name = "user_id" ) )
    private List<User> users = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="groups_rights_id")
    private GroupRights groupRights;

    @JsonIgnore
    @OneToOne
    @JoinTable( name = "link_groups_to_server",
            joinColumns = @JoinColumn( name ="groups_id"),
            inverseJoinColumns = @JoinColumn( name ="server_id" ))
    private Server server;

    public enum TypeOfGroup{
        DISCOODLE,
        ESTABLISHMENT,
        FACULTY,
        ADMINISTRATION,
        SUBJECTS,
        CLASS
    }
}
