package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PersonService implements CrudService<Person> {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional
    public Person create(Person personToSave) {
        Date now = new Date();
        personToSave.setCreatedAt(now);
        personToSave.setUpdatedAt(now);
        personToSave.setEnabled(true);
        personToSave.setId(personRepository.save(personToSave).getId());

        return personToSave;
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Person get(int id) {
        return personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
    }

    @Override
    @Transactional
    public Person update(int id, Person updatedPerson) {
        Person personToUpdate = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personToUpdate.setUsername(updatedPerson.getUsername());
        personToUpdate.setName(updatedPerson.getName());
        personToUpdate.setEmail(updatedPerson.getEmail());
        personToUpdate.setEnabled(updatedPerson.getEnabled());
        personToUpdate.setUpdatedAt(new Date());

        personRepository.save(personToUpdate);

        return personToUpdate;
    }

    @Override
    @Transactional
    public void delete(int id) {
        Person personToDelete = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personRepository.deleteById(personToDelete.getId());
    }
}
