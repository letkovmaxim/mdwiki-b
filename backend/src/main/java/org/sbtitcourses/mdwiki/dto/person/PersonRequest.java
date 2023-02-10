package org.sbtitcourses.mdwiki.dto.person;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO сущности {@link org.sbtitcourses.mdwiki.model.Person} для запроса.
 */
public class PersonRequest {

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

    /**
     * Статус активности аккаунта.
     */
    private boolean enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
