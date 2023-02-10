package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.EntityFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementAlreadyExistsException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Сервис с логикой CRUD операций над сущностью Person.
 */
@Service
@Transactional(readOnly = true)
public class PersonService implements IPersonService {

    /**
     * Компонент для получения сущностей.
     */
    private final EntityFetcher entityFetcher;

    /**
     * Репозиторий для взаимодействия с сущностью Person.
     */
    private final PersonRepository personRepository;

    /**
     * Конструктор для автоматичекого внедрения зависимостей.
     *
     * @param entityFetcher    компонент для получения ресурсов.
     * @param personRepository репозиторий для взаимодействия с сущностью Person.
     */
    @Autowired
    public PersonService(EntityFetcher entityFetcher,
                         PersonRepository personRepository) {
        this.entityFetcher = entityFetcher;
        this.personRepository = personRepository;
    }

    /**
     * Метод, отвечающий за получение всех пользователей.
     *
     * @return список всех пользователей.
     */
    @Override
    public List<Person> get(int bunch, int size) {
        Pageable pageable = PageRequest.of(bunch, size);

        return personRepository.findAll(pageable).getContent();
    }

    /**
     * Метод, отвечающий за получение пользователя по его ID.
     *
     * @param id ID пользователя.
     * @return найденого пользователя.
     * @throws ElementNotFoundException если пользователя с таким ID не существует.
     */
    @Override
    public Person get(int id) {
        Person user = entityFetcher.getLoggedInUser();

        if (user.getId() != id) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        return personRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
    }

    /**
     * Метод, отвечающий за обновление пользователя по его ID.
     *
     * @param id            ID пользователя.
     * @param updatedPerson объект класса Person, значениями полей которого нужно обновить пользователя.
     * @return обновленного пользователя.
     * @throws ElementNotFoundException если пользователя с таким ID не существует.
     */
    @Override
    @Transactional
    public Person update(int id, Person updatedPerson) {
        Person user = entityFetcher.getLoggedInUser();

        if (user.getId() != id) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        if (personRepository.findByEmail(updatedPerson.getEmail()).isPresent()) {
            throw new ElementAlreadyExistsException("Почта уже используется");
        }

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
        person.setUsername(updatedPerson.getUsername());
        person.setName(updatedPerson.getName());
        person.setEmail(updatedPerson.getEmail());
        person.setEnabled(updatedPerson.isEnabled());
        person.setUpdatedAt(new Date());

        personRepository.save(person);

        return person;
    }

    /**
     * Метод, отвечающий за обновление заметки пользователя по его ID.
     *
     * @param id   ID пользователя.
     * @param note обновленная записка.
     * @return обновленного пользователя.
     * @throws ElementNotFoundException если пользователя с таким ID не существует.
     */
    @Override
    @Transactional
    public Person noteUpdate(int id, String note) {
        Person user = entityFetcher.getLoggedInUser();

        if (user.getId() != id) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
        person.setNote(note);

        personRepository.save(person);

        return person;
    }

    /**
     * Метод, отвечающий за удаление пользователя по его ID.
     *
     * @param id ID пользователя.
     * @throws ElementNotFoundException если пользователя с таким ID не существует.
     */
    @Override
    @Transactional
    public void delete(int id) {
        Person user = entityFetcher.getLoggedInUser();

        if (user.getId() != id) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        Person personToDelete = personRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));

        personRepository.delete(personToDelete);
    }
}
