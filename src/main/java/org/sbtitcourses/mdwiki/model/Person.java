package org.sbtitcourses.mdwiki.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import static javax.persistence.GenerationType.IDENTITY;

/**
 * Данный класс описывает какие данные необходимо ввести юзеру при регистрации
 * (role и active заполняются автоматически в RegistrationService.register)
 */
@Entity
@Table(name = "person")
public class Person {

    /**
     * id пользователя в базе данных
     */
    @Id
    @NotNull
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Имя пользователя
     */
    @NotEmpty(message = "Поле имя не должно быть пустым")
    @Column(name = "name")
    private String name;

    /**
     * Логин пользователя
     */
    @NotEmpty(message = "Поле логин не должно быть пустым")
    @Column(name = "username")
    private String username;

    /**
     * Email пользователя
     */
    @Email(message = "Неккоректно введен email")
    @NotEmpty(message = "Поле email не должно быть пустым")
    @Column(name = "email")
    private String email;

    /**
     * Пароль пользователя
     */
    @NotEmpty(message = "Поле пароль не должно быть пустым")
    @Size(min = 6, max = 100, message = "Пароль должен состоять минимум из 6 символов")
    @Column(name = "password")
    private String password;

    /**
     * Роль пользователя
     */
    @Column(name = "role")
    private String role;

    /**
     * Тип аккаунта
     * Активный (true) или заблокированный (false)
     */
    @Column(name = "active")
    private Boolean active;

    /**
     * Создание экземпляра класса
     */
    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
