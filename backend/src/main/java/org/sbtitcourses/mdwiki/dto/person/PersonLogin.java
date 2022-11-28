package org.sbtitcourses.mdwiki.dto.person;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO сущности Person для логина
 */
public class PersonLogin {

    /**
     * Логин или email пользователя
     */
    @NotEmpty(message = "Логин или email не должен быть пустым")
    private String usernameOrEmail;

    /**
     * Пароль пользователя
     */
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 6, message = "Пароль не должен быть короче 6 символов")
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
