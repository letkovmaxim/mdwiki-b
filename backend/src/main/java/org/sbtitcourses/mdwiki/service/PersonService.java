package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.exception.PersonNotFoundException;
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
     * Класс для шифрования пароля
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     *
     * @param personRepository репозиторий для взаимодействия с сущностью Person
     * @param passwordEncoder
     */
    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Метод, отвечающий за создание нового пользователя
     * @param personToSave пользователь, которого нужно сохранить
     * @return сохраненного пользователя
     */
    @Override
    @Transactional
    public Person create(Person personToSave) {
        Date now = new Date();
        personToSave.setPassword(passwordEncoder.encode(personToSave.getPassword()));
        personToSave.setCreatedAt(now);
        personToSave.setUpdatedAt(now);
        personToSave.setEnabled(true);
        personToSave.setId(personRepository.save(personToSave).getId());
        personToSave.setRole("ROLE_USER");

        return personToSave;
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
     * @throws PersonNotFoundException если пользователя с таким ID не существует
     */
    @Override
    public Person get(int id) throws PersonNotFoundException {
        return personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Метод, отвечающий за получение пользователя по его username
     * @param username username пользователя
     * @return найденого пользователя
     * @throws PersonNotFoundException если пользователя с таким username не существует
     */
    @Override
    public Person get(String username) {
        return personRepository.findByUsername(username).orElseThrow(PersonNotFoundException::new);
    }

    /**
     * Метод, отвечающий за обновление пользователя по его ID
     * @param id ID пользователя
     * @param updatedPerson объект класса Person, значениями полей которого нужно обновить пользователя
     * @return обновленного пользователя
     * @throws PersonNotFoundException если пользователя с таким ID не существует
     */
    @Override
    @Transactional
    public Person update(int id, Person updatedPerson) throws PersonNotFoundException {
        Person personToUpdate = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personToUpdate.setUsername(updatedPerson.getUsername());
        personToUpdate.setName(updatedPerson.getName());
        personToUpdate.setEmail(updatedPerson.getEmail());
        personToUpdate.setEnabled(updatedPerson.getEnabled());
        personToUpdate.setUpdatedAt(new Date());

        personRepository.save(personToUpdate);

        return personToUpdate;
    }

    /**
     * Метод, отвечающий за удаление пользователя по его ID
     * @param id ID пользователя
     * @throws PersonNotFoundException если пользователя с таким ID не существует
     */
    @Override
    @Transactional
    public void delete(int id) throws PersonNotFoundException {
        Person personToDelete = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
        personRepository.delete(personToDelete);
    }
}
