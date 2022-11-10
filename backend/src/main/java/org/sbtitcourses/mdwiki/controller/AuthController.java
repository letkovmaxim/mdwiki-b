package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.person.PersonLogin;
import org.sbtitcourses.mdwiki.dto.person.PersonRequest;
import org.sbtitcourses.mdwiki.dto.person.PersonResponse;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.service.PersonService;
import org.sbtitcourses.mdwiki.service.security.PersonDetailsService;
import org.sbtitcourses.mdwiki.util.ErrorResponse;
import org.sbtitcourses.mdwiki.util.PersonValidator;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Контроллер для страниц login и registration
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Валидатор, который проверяет введенные поля пользоватлем
     */
    private final PersonValidator personValidator;

    /**
     * Маппер для конвертации сущностей
     */
    private final ModelMapper modelMapper;

    /**
     * Сервис с логикой CRUD операций над сущностью Person
     */
    private final PersonService personService;

    /**
     * Сервис для Spring Security, загружает пользователя
     */
    private final PersonDetailsService personDetailsService;



    /**
     * Инициализация полей класса
     */
    @Autowired
    public AuthController(PersonValidator personValidator, ModelMapper modelMapper, PersonService personService, PersonDetailsService personDetailsService) {
        this.personValidator = personValidator;
        this.modelMapper = modelMapper;
        this.personService = personService;
        this.personDetailsService = personDetailsService;
    }

    /**
     * Метод для авторизации пользователя
     * @param personLogin, логин или mail и пароль пользователя
     * @return если пользователь успешно авторизирован статус 200, в противном случае 403
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginPage(@RequestBody PersonLogin personLogin){

        UserDetails userDetails;

        try{
            personValidator.checkPassword(personLogin.getUsernameOrLogin(), personLogin.getPassword());
            userDetails = personDetailsService.loadUserByUsername(personLogin.getUsernameOrLogin());
        }catch (ElementNotFoundException ex){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()) ;
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Метод для получения авторизированного пользователя
     * @return авторизированного пользователя
     */
    @GetMapping("/whoami")
    public ResponseEntity<PersonResponse> whoAmI() {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Person person = personService.get(currentUserName);

        PersonResponse response = modelMapper.map(person, PersonResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за регистрацию нового пользователя
     * @param personRequest DTO сущности Person для запроса
     * @return DTO сущности Person для ответа с кодом 201
     */
    @PostMapping("/registration")
    public ResponseEntity<Object> performRegistration(@RequestBody @Valid PersonRequest personRequest) {
        Person personToCreate = modelMapper.map(personRequest, Person.class);

        ErrorResponse errors = personValidator.validate(personToCreate.getUsername(), personToCreate.getEmail());

        if(!errors.getErrors().isEmpty()){
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        Person createdPerson = personService.create(personToCreate);

        PersonResponse response = modelMapper.map(createdPerson, PersonResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, отвечающий за выход пользователя
     */
    @PostMapping("/logout")
    public void logout(){SecurityContextHolder.getContext().setAuthentication(null);}

}
