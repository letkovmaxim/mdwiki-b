package org.sbtitcourses.mdwiki.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static org.hibernate.annotations.CascadeType.ALL;

/**
 * Сущность, описывающая зарегестрированного пользователя.
 */
@Entity
@Table(name = "persons")
public class Person implements Serializable {

    private static final long serialVersionUID = 8080395540442381101L;

    /**
     * ID пользователя в базе данных.
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Логин пользователя.
     */
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Пароль пользователя.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Роль пользователя.
     */
    @Column(name = "role", nullable = false)
    private String role;

    /**
     * Имя пользователя.
     */
    @Column(name = "name")
    private String name;

    /**
     * Эл. почта пользователя.
     */
    @Column(name = "email")
    private String email;

    /**
     * Точное время создания пользователя.
     */
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Точное время обновления пользователя.
     */
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Статус активности аккаунта.
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    /**
     * Текст заметки markdown-документа.
     */
    @Column(name = "note")
    private String note;

    /**
     * Список пространств, принадлежащих пользователю.
     */
    @OneToMany(mappedBy = "owner", fetch = LAZY)
    @Cascade(ALL)
    private List<Space> spaces;

    /**
     * Список хранимых файлов, принадлежащих пользователю.
     */
    @OneToMany(mappedBy = "owner", fetch = LAZY)
    @Cascade(ALL)
    private List<StoredFile> storedFiles;

    public Person() {
    }

    public static PersonBuilder builder() {
        return new PersonBuilder();
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public static final class PersonBuilder {
        private final Person person;

        private PersonBuilder() {
            person = new Person();
        }

        public PersonBuilder id(int id) {
            person.setId(id);
            return this;
        }

        public PersonBuilder username(String username) {
            person.setUsername(username);
            return this;
        }

        public PersonBuilder password(String password) {
            person.setPassword(password);
            return this;
        }

        public PersonBuilder role(String role) {
            person.setRole(role);
            return this;
        }

        public PersonBuilder name(String name) {
            person.setName(name);
            return this;
        }

        public PersonBuilder email(String email) {
            person.setEmail(email);
            return this;
        }

        public PersonBuilder createdAt(Instant createdAt) {
            person.setCreatedAt(createdAt);
            return this;
        }

        public PersonBuilder updatedAt(Instant updatedAt) {
            person.setUpdatedAt(updatedAt);
            return this;
        }

        public PersonBuilder enabled(boolean enabled) {
            person.setEnabled(enabled);
            return this;
        }

        public PersonBuilder note(String note) {
            person.setNote(note);
            return this;
        }

        public PersonBuilder spaces(List<Space> spaces) {
            person.setSpaces(spaces);
            return this;
        }

        public PersonBuilder storedFiles(List<StoredFile> storedFiles) {
            person.setStoredFiles(storedFiles);
            return this;
        }

        public Person build() {
            return person;
        }
    }
}