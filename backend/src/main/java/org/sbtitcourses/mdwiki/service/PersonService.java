package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Сервис с логикой CRUD операций над сущностью Person
 */
@Service
@Transactional(readOnly = true)
public class PersonService implements PersonCrudService {

    /**
     * Репозиторий для взаимодействия с сущностью Person
     */
    private final PersonRepository personRepository;

    /**
     * Компонент для шифрования пароля
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param personRepository репозиторий для взаимодействия с сущностью Person
     * @param passwordEncoder компонент для шифрования пароля
     */
    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Метод, отвечающий за создание нового пользователя
     * @param person пользователь, которого нужно сохранить
     * @return сохраненного пользователя
     */
    @Override
    @Transactional
    public Person create(Person person) {
        Date now = new Date();
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setCreatedAt(now);
        person.setUpdatedAt(now);
        person.setEnabled(true);
        person.setRole(person.getRole());
        person.setId(personRepository.save(person).getId());

        return person;
    }

    /**
     * Метод, отвечающий за получение всех пользователей
     * @return список всех пользователей
     */
    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    /**
     * Метод, отвечающий за получение пользователя по его ID
     * @param id ID пользователя
     * @return найденого пользователя
     * @throws ElementNotFoundException если пользователя с таким ID не существует
     */
    @Override
    public Person get(int id) throws ElementNotFoundException {
        return personRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
    }

    /**
     * Метод, отвечающий за обновление пользователя по его ID
     * @param id ID пользователя
     * @param updatedPerson объект класса Person, значениями полей которого нужно обновить пользователя
     * @return обновленного пользователя
     * @throws ElementNotFoundException если пользователя с таким ID не существует
     */
    @Override
    @Transactional
    public Person update(int id, Person updatedPerson) throws ElementNotFoundException {
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
     * Метод, отвечающий за обновление заметки пользователя по его ID
     * @param id ID пользователя
     * @param note обновленная записка
     * @return обновленного пользователя
     * @throws ElementNotFoundException если пользователя с таким ID не существует
     */
    @Override
    @Transactional
    public Person noteUpdate(int id, String note) throws ElementNotFoundException {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
        person.setText(note);

        personRepository.save(person);

        return person;
    }

    /**
     * Метод, отвечающий за удаление пользователя по его ID
     * @param id ID пользователя
     * @throws ElementNotFoundException если пользователя с таким ID не существует
     */
    @Override
    @Transactional
    public void delete(int id) throws ElementNotFoundException {
        Person personToDelete = personRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));

        personRepository.delete(personToDelete);
    }
}
