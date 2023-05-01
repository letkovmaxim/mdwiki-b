package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.person.PersonLogin;
import org.sbtitcourses.mdwiki.dto.person.PersonRegistration;
import org.sbtitcourses.mdwiki.dto.person.PersonResponse;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.service.security.EntryService;
import org.sbtitcourses.mdwiki.util.EntityFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * REST контроллер, обрабатывающий запросы на вход и регистрацию пользователя.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Сервис с логикой входа и регистрации пользователя.
     */
    private final EntryService entryService;

    /**
     * Компонент для получения ресурсов.
     */
    private final EntityFetcher entityFetcher;

    /**
     * Маппер для конвертации сущностей.
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     *
     * @param entryService  сервис с логикой входа и регистрации пользователя.
     * @param entityFetcher компонент для получения ресурсов.
     * @param modelMapper   маппер для конвертации сущностей.
     */
    @Autowired
    public AuthController(EntryService entryService,
                          EntityFetcher entityFetcher,
                          ModelMapper modelMapper) {
        this.entryService = entryService;
        this.entityFetcher = entityFetcher;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, обрабатывающий запрос на авторизацию пользователя.
     *
     * @param personLogin DTO сущности {@link Person} для логина.
     * @return HTTP ответ со статусом 200.
     */
    @PostMapping("/login")
    public ResponseEntity<HttpStatus> performLogin(@RequestBody @Valid PersonLogin personLogin) {
        entryService.login(personLogin);

        return ResponseEntity.ok().build();
    }

    /**
     * Метод, обрабатывающий запрос на получение авторизированного пользователя.
     *
     * @return HTTP ответ с информацией об авторизованном пользователе и статусом 200.
     */
    @GetMapping("/whoami")
    public ResponseEntity<PersonResponse> whoAmI() {
        Person person = entityFetcher.getLoggedInUser();

        PersonResponse response = modelMapper.map(person, PersonResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на регистрацию нового пользователя.
     *
     * @param personRegistration DTO сущности {@link Person} для регистрации.
     * @return HTTP ответ с информацией о новом пользователе и статусом 201.
     */
    @PostMapping("/registration")
    public ResponseEntity<Object> performRegistration(@RequestBody @Valid PersonRegistration personRegistration) {
        Person person = modelMapper.map(personRegistration, Person.class);

        Person registeredPerson = entryService.register(person);

        PersonResponse response = modelMapper.map(registeredPerson, PersonResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, обрабатывающий запрос на выход пользователя.
     */
    @PostMapping("/logout")
    public void logout() {
        entryService.logout();
    }
}
