package org.sbtitcourses.mdwiki.security;

import org.sbtitcourses.mdwiki.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

/**
 * Обертка над сущностью, для работы не напрямую с Person
 * Стандартные методы
 */
public class PersonDetails implements UserDetails {

    /**
     * Данные пользователя
     */
    private final Person person;

    /**
     * Инициализация поля
     */
    public PersonDetails(Person person) {
        this.person = person;
    }

    /**
     * @return возвращает полномочия, предоставленные пользователю
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole()));
    }

    /**
     * @return возвращает пароль, используемый для аутентификации пользователя
     */
    @Override
    public String getPassword() {
        return this.person.getPassword();
    }

    /**
     * @return возвращает имя пользователя, используемое для аутентификации пользователя
     */
    @Override
    public String getUsername() {
        return this.person.getUsername();
    }

    /**
     * Указывает, истек ли срок действия учетной записи пользователя. Учетная запись с истекшим сроком действия не может быть аутентифицирована.
     * @return учетная запись пользователя действительна
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Указывает, заблокирован или разблокирован пользователь. Заблокированный пользователь не может быть аутентифицирован.
     * @return пользователь не заблокирован
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Указывает, истек ли срок действия учетных данных пользователя
     * @return учетные данные пользователя действительны
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Указывает, включен или отключен пользователь
     * @return пользователь включен
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
