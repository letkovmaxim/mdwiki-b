package org.sbtitcourses.mdwiki.dto.person;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Person} для логина.
 */
public class PersonLogin {

    /**
     * Логин или эл. почта пользователя.
     */
    @NotEmpty
    private String usernameOrEmail;

    /**
     * Пароль пользователя.
     */
    @NotEmpty
    @Size(min = 6)
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
