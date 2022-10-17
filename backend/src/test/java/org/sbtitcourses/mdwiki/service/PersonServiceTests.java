package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.exception.PersonNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTests {

    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonService personService;

    @Test
    public void createShouldReturnPerson() {
        Person personToCreate = new Person();
        when(personRepository.save(personToCreate)).thenReturn(new Person(1));

        Person createdPerson = personService.create(personToCreate);

        assertEquals(1, createdPerson.getId());
        assertTrue(createdPerson.getEnabled());
        verify(personRepository).save(personToCreate);
    }

    @Test
    public void getAllShouldReturnPersonList() {
        List<Person> people = new LinkedList<>();
        people.add(new Person(1));
        people.add(new Person(2));
        when(personRepository.findAll()).thenReturn(people);

        List<Person> gottenPeople = personService.getAll();

        assertEquals(2, gottenPeople.size());
        assertEquals(1, gottenPeople.get(0).getId());
        assertEquals(2, gottenPeople.get(1).getId());
        verify(personRepository).findAll();
    }

    @Test
    public void getShouldReturnPerson() {
        when(personRepository.findById(1)).thenReturn(Optional.of(new Person(1)));
        when(personRepository.findById(2)).thenReturn(Optional.empty());

        Person gottenPerson = personService.get(1);

        assertEquals(1, gottenPerson.getId());
        assertThrows(PersonNotFoundException.class, () -> personService.get(2));
        verify(personRepository).findById(1);
    }

    @Test
    public void updateShouldReturnPerson() {
        Person personToUpdate = new Person(1);
        Person newPerson = new Person();
        newPerson.setUsername("newUsername");
        newPerson.setName("newName");
        newPerson.setEmail("newEmail@mail.com");
        newPerson.setEnabled(true);
        when(personRepository.findById(1)).thenReturn(Optional.of(personToUpdate));
        when(personRepository.findById(2)).thenReturn(Optional.empty());

        Person updatedPerson = assertDoesNotThrow(() -> personService.update(1, newPerson));

        assertEquals(1, updatedPerson.getId());
        assertEquals("newUsername", updatedPerson.getUsername());
        assertEquals("newName", updatedPerson.getName());
        assertEquals("newEmail@mail.com", updatedPerson.getEmail());
        assertTrue(updatedPerson.getEnabled());
        assertThrows(PersonNotFoundException.class, () -> personService.update(2, newPerson));
        verify(personRepository).findById(1);
        verify(personRepository).save(personToUpdate);
        verify(personRepository).findById(2);
    }

    @Test
    public void deleteShouldRemovePerson() {
        when(personRepository.findById(1)).thenReturn(Optional.of(new Person(1))).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> personService.delete(1));
        assertThrows(PersonNotFoundException.class, () -> personService.get(1));
        verify(personRepository, times(2)).findById(1);
        verify(personRepository).deleteById(1);
    }
}