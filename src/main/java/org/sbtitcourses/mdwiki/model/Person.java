package org.sbtitcourses.mdwiki.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Фамилия не должно быть пустым")
    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @NotEmpty(message = "username не должно быть пустым")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "email не должно быть пустым")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Пароль не может быть пустым")
    @Size(min = 6, max = 100, message = "Пароль должен состоять минимум из 6 символов")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "active")
    private Boolean active;

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
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
