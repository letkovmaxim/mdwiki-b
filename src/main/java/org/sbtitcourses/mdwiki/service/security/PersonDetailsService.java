package org.sbtitcourses.mdwiki.service.security;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.security.PersonDetails;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Сервис с логикой загрузки данных пользователя.
 */
@Service
public class PersonDetailsService implements UserDetailsService {

    /**
     * Репозиторий для взаимодействия с сущностью {@link Person}.
     */
    private final PersonRepository personRepository;

    /**
     * Конструктор для автоматичекого внедрения зависимостей.
     *
     * @param personRepository репозиторий для взаимодействия с сущностью {@link Person}.
     */
    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Метод, отвечающий за загрузку данных пользователя.
     *
     * @param usernameOrEmail логин или эл. почта пользователя.
     * @return объект {@link PersonDetails} с данными о пользователе.
     * @throws ElementNotFoundException если пользователь не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        Person person = personRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));

        return new PersonDetails(person);
    }
}
