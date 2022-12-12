package org.sbtitcourses.mdwiki.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.CascadeType.ALL;

/**
 * Сущность, описывающая зарегестрированных пользователей
 */
@Entity
@Table(name = "persons")
public class Person {

    /**
     * ID пользователя в базе данных
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Логин пользователя
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Пароль пользователя
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Роль пользователя
     */
    @Column(name = "role", nullable = false)
    private String role;

    /**
     * Имя пользователя
     */
    @Column(name = "name")
    private String name;

    /**
     * Email пользователя
     */
    @Column(name = "email")
    private String email;

    /**
     * Точное время создания пользователя
     */
    @Temporal(TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    /**
     * Точное время обновления пользователя
     */
    @Temporal(TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    /**
     * Тип аккаунта
     * Активный (true) или заблокированный (false)
     */
    @Column(name = "enabled", nullable = false)
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

    public Person() {
    }

    public Person(int id) {
        this.id = id;
    }

    public Person(String password) {
        this.password = password;
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
