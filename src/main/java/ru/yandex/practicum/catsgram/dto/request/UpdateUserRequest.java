package ru.yandex.practicum.catsgram.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UpdateUserRequest extends NewUserRequest {

    private String username;
    private String password;
    private String email;

    public boolean hasUsername() {
        return username != null;
    }

    public boolean hasEmail() {
        return email != null;
    }

    public boolean hasPassword() {
        return password != null;
    }

}
