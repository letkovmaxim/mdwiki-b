package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.PersonResponse;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Person create(@RequestBody PersonResponse personResponse) {
        Person personToCreate = modelMapper.map(personResponse, Person.class);

        return personService.create(personToCreate);
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
    public Person update(@PathVariable int id, @RequestBody PersonResponse updatedPersonResponse) {
        Person personToUpdate = modelMapper.map(updatedPersonResponse, Person.class);

        return personService.update(id, personToUpdate);
    }
}
