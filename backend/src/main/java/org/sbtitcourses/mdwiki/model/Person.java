package org.sbtitcourses.mdwiki.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.CascadeType.ALL;

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
    @GeneratedValue(strategy = SEQUENCE, generator = "personSequence")
    @SequenceGenerator(name = "personSequence", initialValue = 1, allocationSize = 1, sequenceName = "person_sequence")
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Логин пользователя
     */
    @NotEmpty(message = "Логин не должен быть пустым")
    @Size(min = 4, max = 50, message = "Логин не должен быть короче 4 и длинее 50 символов")
    @Column(name = "username")
    private String username;

    /**
     * Пароль пользователя
     */
    @NotEmpty(message = "Пароль не должен быть пустым")
    @Size(min = 6, message = "Пароль не должен быть короче 6 символов")
    @Column(name = "password")
    private String password;

    /**
     * Роль пользователя
     */
    @Column(name = "role")
    private String role;

    /**
     * Имя пользователя
     */
    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(max = 128, message = "Имя не должно быть длинее 128 символов")
    @Column(name = "name")
    private String name;

    /**
     * Email пользователя
     */
    @NotEmpty(message = "Email не должен быть пустым")
    @Email(message = "Email введен неккоректно")
    @Column(name = "email")
    private String email;

    /**
     * Точное время создания пользователя
     */
    @Temporal(TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    /**
     * Точное время обновления пользователя
     */
    @Temporal(TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Тип аккаунта
     * Активный (true) или заблокированный (false)
     */
    @Column(name = "is_enabled")
    private boolean enabled;

    /**
     * Список пространств, принадлежащих пользователю
     */
    @OneToMany(mappedBy = "owner", fetch = LAZY)
    @Cascade(ALL)
    private List<Space> spaces;

    /**
     * Список хранимых файлов, принадлежащих пользователю
     */
    @OneToMany(mappedBy = "owner", fetch = LAZY)
    @Cascade(ALL)
    private List<StoredFile> storedFiles;

    /**
     * Создание экземпляра класса
     */
    public Person() {
    }

    public Person(int id) {
        this.id = id;
    }

    public Person(int id, String username, String name, String email, boolean enabled) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
        this.enabled = enabled;
    }

    public Person(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Space> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<Space> spaces) {
        this.spaces = spaces;
    }

    public List<StoredFile> getStoredFiles() {
        return storedFiles;
    }

    public void setStoredFiles(List<StoredFile> storedFiles) {
        this.storedFiles = storedFiles;
    }
}
