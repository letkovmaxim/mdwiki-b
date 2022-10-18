package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.PersonRequest;
import org.sbtitcourses.mdwiki.dto.PersonResponse;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.service.PersonService;
import org.sbtitcourses.mdwiki.util.ErrorResponse;
import org.sbtitcourses.mdwiki.util.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private final ModelMapper modelMapper;

    @Autowired
    public PersonController(PersonService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public PersonResponse create(@RequestBody PersonRequest personRequest) {
        Person personToCreate = modelMapper.map(personRequest, Person.class);

        return modelMapper.map(personService.create(personToCreate), PersonResponse.class);
    }

    @GetMapping
    public List<PersonResponse> getAll() {
        List<PersonResponse> people = new LinkedList<>();
        for (Person person: personService.getAll()) {
            people.add(modelMapper.map(person, PersonResponse.class));
        }

        return people;
    }

    @GetMapping("/{id}")
    public PersonResponse get(@PathVariable int id) {
        Person person = personService.get(id);

        return modelMapper.map(person, PersonResponse.class);
    }

    @PutMapping("/{id}")
    public PersonResponse update(@PathVariable int id, @RequestBody PersonRequest personRequest) {
        Person updatedPerson = modelMapper.map(personRequest, Person.class);

        return modelMapper.map(personService.update(id, updatedPerson), PersonResponse.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        personService.delete(id);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Человек не найден",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
