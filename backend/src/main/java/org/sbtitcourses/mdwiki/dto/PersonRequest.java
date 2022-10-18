package org.sbtitcourses.mdwiki.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class PersonRequest {

    /**
     * Логин пользователя
     */
    @NotEmpty(message = "Логин не должен быть пустым")
    @Size(min = 4, max = 50, message = "Логин не должен быть короче 4 и длинее 50 символов")
    private String username;

    private String password;

    /**
     * Имя пользователя
     */
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(max = 128, message = "Имя не должно быть длинее 128 символов")
    private String name;

    /**
     * Email пользователя
     */
    @NotEmpty(message = "Email не должен быть пустым")
    @Email(message = "Email введен неккоректно")
    private String email;

    /**
     * Тип аккаунта
     * Активный (true) или заблокированный (false)
     */
    private Boolean isEnabled;

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

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
