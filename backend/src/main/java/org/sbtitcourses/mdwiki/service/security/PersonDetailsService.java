package org.sbtitcourses.mdwiki.service.security;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.security.PersonDetails;
import org.sbtitcourses.mdwiki.util.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
     * @throws PersonNotFoundException если пользователь не найден с таким email или логин
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws PersonNotFoundException {
        Optional<Person> person = Optional.ofNullable(personRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(PersonNotFoundException::new));

        return new PersonDetails(person.get());
    }
}
