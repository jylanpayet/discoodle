package com.discoodle.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private final String mail;

    private final String username;

    private final String password;

    private final String name;

    private final String lastName;

    private final Date birthDay;

    private final String linkToAvatar;

    private final UserRole role;
}
