package org.sbtitcourses.mdwiki.service.security;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.security.PersonDetails;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис для Spring Security, загружает пользователя
 */
@Service
public class PersonDetailsService implements UserDetailsService {

    /**
     * Поиск в базе данных
     */
    private final PersonRepository personRepository;

    /**
     * Инициализация поля
     */
    @Autowired
    public PersonDetailsService(PersonRepository peopleRepository) {
        this.personRepository = peopleRepository;
    }

    /**
     * Загрузка пользователя
     * @param usernameOrEmail Email или логин пользователя
     * @return объект PersonDetails, который содержит некоторые методы для описания информации о пользователе
     * @throws ElementNotFoundException если пользователь не найден с таким email или логин
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail){
        Person person = personRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));

        return new PersonDetails(person);
    }
}
