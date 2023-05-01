package org.sbtitcourses.mdwiki.security;

import org.sbtitcourses.mdwiki.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Класс, предоставляющий основную информацию о пользователе.
 */
public class PersonDetails implements UserDetails {

    private static final long serialVersionUID = 8595142225241536052L;

    /**
     * Объект сущности пользователя.
     */
    private final Person person;

    /**
     * Конструктор для создания объекта класса.
     *
     * @param person объект сущности пользователя.
     */
    public PersonDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return person.isEnabled();
    }
}
