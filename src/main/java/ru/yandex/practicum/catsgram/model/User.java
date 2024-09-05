package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = "email")
public class User {

    protected Long id;
    protected String username;
    protected String email;
    protected String password;
    protected Instant registrationDate;

}
