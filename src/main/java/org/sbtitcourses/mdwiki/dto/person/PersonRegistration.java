package org.sbtitcourses.mdwiki.dto.person;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Person} для регистрации.
 */
public class PersonRegistration {

    /**
     * Логин пользователя.
     */
    @NotEmpty
    @Size(min = 4, max = 50)
    private String username;

    /**
     * Пароль пользователя.
     */
    @NotEmpty
    @Size(min = 6)
    private String password;

    /**
     * Имя пользователя.
     */
    @NotEmpty
    @Size(max = 128)
    private String name;

    /**
     * Эл. почта пользователя.
     */
    @NotEmpty
    @Email
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
