package com.discoodle.api.request;

import com.discoodle.api.model.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private final String mail;

    private final String username;

    private final String password;

    private final String name;

    private final String last_name;

    private final String link_to_avatar;

    private final User.Role role;

}
