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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.LinkedList;
import java.util.List;

/**
 * REST контроллер, обрабатывающий запросы на взаимодействие с сущностью {@link Person}.
 */
@RestController
@RequestMapping("/people")
@Validated
public class PersonController {

    /**
     * Сервис с логикой взаимодействия с сущностью {@link Person}.
     */
    private final PersonService personService;

    /**
     * Маппер для конвертации сущностей.
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей.
     *
     * @param personService сервис с логикой взаимодействия с сущностью {@link Person}.
     * @param modelMapper   маппер для конвертации сущностей.
     */
    @Autowired
    public PersonController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, обрабатывающий запрос на получение всех пользователей.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов на странице при пагинации.
     * @return HTTP ответ со списком пользователей и статусом 200.
     */
    @GetMapping
    public ResponseEntity<List<PersonResponse>> get(@RequestParam("bunch") @Min(0) int bunch,
                                                    @RequestParam("size") @Min(1) int size) {
        List<PersonResponse> people = new LinkedList<>();

        for (Person person : personService.get(bunch, size)) {
            PersonResponse response = modelMapper.map(person, PersonResponse.class);
            people.add(response);
        }

        return ResponseEntity.ok().body(people);
    }

    /**
     * Метод, обрабатывающий запрос на получение польвателя по его ID.
     *
     * @param id ID пользователя.
     * @return HTTP ответ с информацией о пользователе и статусом 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> get(@PathVariable("id") int id) {
        Person person = personService.get(id);

        PersonResponse response = modelMapper.map(person, PersonResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на обновление пользователя по его ID.
     *
     * @param id            ID пользователя.
     * @param personRequest информация о пользователе, которую нужно обновить.
     * @return HTTP ответ с информацией об обновленном пользователе и статусом 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(@PathVariable("id") int id,
                                                 @RequestBody @Valid PersonRequest personRequest) {
        Person personToUpdateWith = modelMapper.map(personRequest, Person.class);

        Person updatedPerson = personService.update(id, personToUpdateWith);

        PersonResponse response = modelMapper.map(updatedPerson, PersonResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на обновление заметки пользователя.
     *
     * @param id                ID пользователя.
     * @param personNoteRequest информация о пользователе, которую нужно обновить.
     * @return HTTP ответ с информацией об обновленном пользователе и статусом 200.
     */
    @PutMapping("/{id}/note")
    public ResponseEntity<PersonResponse> noteUpdate(@PathVariable("id") int id,
                                                     @RequestBody @Valid PersonNoteRequest personNoteRequest) {
        Person updatedPerson = personService.noteUpdate(id, personNoteRequest.getText());

        PersonResponse response = modelMapper.map(updatedPerson, PersonResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на удаление пользователя по его ID.
     *
     * @param id ID пользователя.
     * @return HTTP ответ со статусом 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        personService.delete(id);

        return ResponseEntity.noContent().build();
    }
}