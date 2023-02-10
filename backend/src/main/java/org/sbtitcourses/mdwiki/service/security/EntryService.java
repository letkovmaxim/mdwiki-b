package org.sbtitcourses.mdwiki.service.security;

import org.sbtitcourses.mdwiki.dto.person.PersonLogin;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.RegistrationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис с логикой входа и регистрации пользователя.
 */
@Service
@Transactional(readOnly = true)
public class EntryService {

    /**
     * Репозиторий для взаимодействия с сущностью {@link Person}.
     */
    private final PersonRepository personRepository;

    /**
     * Сервис с логикой загрузки данных пользователя.
     */
    private final PersonDetailsService personDetailsService;

    /**
     * Компонент для шифрования пароля.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для автоматичекого внедрения зависимостей.
     *
     * @param personRepository     репозиторий для взаимодействия с сущностью {@link Person}.
     * @param personDetailsService сервис с логикой загрузки данных пользователя.
     * @param passwordEncoder      компонент для шифрования пароля.
     */
    @Autowired
    public EntryService(PersonRepository personRepository,
                        PersonDetailsService personDetailsService,
                        PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.personDetailsService = personDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Метод, отвечающий за регистрацию пользователя.
     *
     * @param person пользователь, которого нужно зарегистрировать.
     * @return зарегистрированного пользователя.
     * @throws RegistrationFailedException если регистрация не удалась.
     */
    @Transactional
    public Person register(Person person) {
        List<String> errors = new ArrayList<>();

        if (personRepository.findByUsername(person.getUsername()).isPresent()) {
            errors.add("Пользователь с таким логином уже существует");
        }

        if (personRepository.findByEmail(person.getEmail()).isPresent()) {
            errors.add("Пользователь с таким адресом эл. почты уже существует");
        }

        if (!errors.isEmpty()) {
            throw new RegistrationFailedException(errors);
        }

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        Instant now = Instant.now();
        person.setCreatedAt(now);
        person.setUpdatedAt(now);
        person.setEnabled(true);
        person.setNote("# Привет, " + person.getUsername());
        person.setId(personRepository.save(person).getId());

        return person;
    }

    /**
     * Метод, отвечающий за вход пользователя.
     *
     * @param personLogin DTO сущности {@link Person} для логина.
     */
    public void login(PersonLogin personLogin) {
        UserDetails userDetails = personDetailsService.loadUserByUsername(personLogin.getUsernameOrEmail());

        if (!passwordEncoder.matches(personLogin.getPassword(), userDetails.getPassword())) {
            throw new AccessDeniedException("Неверный пароль");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Метод, отвечаюзий за выход пользователя.
     */
    public void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
