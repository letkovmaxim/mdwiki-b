package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.person.PersonNoteRequest;
import org.sbtitcourses.mdwiki.dto.person.PersonRequest;
import org.sbtitcourses.mdwiki.dto.person.PersonResponse;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

/**
 * REST контроллер для CRUD операций над сущностью Person
 */
@RestController
@RequestMapping("/people")
public class PersonController {

    /**
     * Сервис с логикой CRUD операций над сущностью Person
     */
    private final PersonService personService;

    /**
     * Маппер для конвертации сущностей
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param personService сервис с логикой CRUD операций над сущностью Person
     * @param modelMapper маппер для конвертации сущностей
     */
    @Autowired
    public PersonController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, отвечающий за создание нового пользователя
     * @param personRequest DTO сущности Person для запроса
     * @return DTO сущности Person для ответа с кодом 201
     */
    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody @Valid PersonRequest personRequest) {
        Person personToCreate = modelMapper.map(personRequest, Person.class);

        Person createdPerson = personService.create(personToCreate);

        PersonResponse response = modelMapper.map(createdPerson, PersonResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, отвечающий за получение всех пользователей
     * @return список DTO сущности Person для ответа с кодом 200
     */
    @GetMapping
    public ResponseEntity<List<PersonResponse>> getAll() {
        List<PersonResponse> people = new LinkedList<>();

        for (Person person: personService.getAll()) {
            PersonResponse response = modelMapper.map(person, PersonResponse.class);
            people.add(response);
        }

        return new ResponseEntity<>(people, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за получение польвателя по его ID
     * @param id ID пользователя
     * @return DTO сущности Person для ответа с кодом 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> get(@PathVariable(name = "id") int id) {
        Person person = personService.get(id);

        PersonResponse response = modelMapper.map(person, PersonResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за обновление пользователя по его ID
     * @param id ID пользователя
     * @param personRequest DTO сущности Person для запроса
     * @return DTO сущности Person для ответа с кодом 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(@PathVariable(name = "id") int id,
                                                 @RequestBody @Valid PersonRequest personRequest) {
        Person personToUpdateWith = modelMapper.map(personRequest, Person.class);

        Person updatedPerson = personService.update(id, personToUpdateWith);

        PersonResponse response = modelMapper.map(updatedPerson, PersonResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за обновление заметки пользователя
     * @param id ID пользователя
     * @param personNoteRequest DTO сущности запроса с новой заметкой
     * @return DTO сущности Person для ответа с кодом 200
     */
    @PutMapping("/{id}/note")
    public ResponseEntity<PersonResponse> noteUpdate(@PathVariable(name = "id") int id,
                                                     @RequestBody @Valid PersonNoteRequest personNoteRequest) {
        Person updatedPerson = personService.noteUpdate(id, personNoteRequest.getNote());

        PersonResponse response = modelMapper.map(updatedPerson, PersonResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за удаление пользователя по его ID
     * @param id ID пользователя
     * @return пустой ответ с кодом 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable(name = "id") int id) {
        personService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}