package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Класс описывающий бизнес логику регистрации
 */
@Service
public class RegistrationService {

    /**
     * Поиск в базе данных
     */
    private final PersonRepository peopleRepository;

    /**
     * Класс для шифрования пароля
     */
    private final PasswordEncoder passwordEncoder;


    /**
     * Инициализация полей
     */
    @Autowired
    public RegistrationService(PersonRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Вызывается при регистрации пользоватеоя
     * Шифрует пароль
     * Добавляет роль user пользователю
     * Делает его аккунт активным(в противном случае он заблокирован)
     * @param person пользователь проходящий регистрацию
     */
    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        person.setRole("ROLE_USER");

        person.setActive(true);

        peopleRepository.save(person);
    }
}
