package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
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
        Date now =  new Date();
        personToSave.setCreatedAt(now);
        personToSave.setUpdatedAt(now);
        personToSave.setEnabled(true);

        return personRepository.save(personToSave);
    }

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Person get(int id) {
        return personRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Person update(int id, Person personToUpdate) {
        personToUpdate.setUpdatedAt(new Date());

        return personRepository.save(personToUpdate);
    }

    @Override
    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }
}
