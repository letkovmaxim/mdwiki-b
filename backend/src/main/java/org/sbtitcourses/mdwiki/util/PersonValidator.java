package org.sbtitcourses.mdwiki.util;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Класс валидатор, который реализует все необходимые проверки полей ввода при регистрации класса Person
 */
@Component
public class PersonValidator implements Validator {

    /**
     * Поиск в базе данных
     */
    private final PersonRepository peopleRepository;

    /**
     * Класс для шифрования пароля
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Инициализая поля
     */
    @Autowired
    public PersonValidator(PersonRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Проверка на поддержку класса валидатором
     * @param clazz проверяемый класс
     * @return значение true, если он соответвсвует Person
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    /**
     * Проверка на уникальность userName и email
     * @param target объект класса Person
     * @param errors добавляются ошибки при не уникальном логине и email
     */
    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if(peopleRepository.findByUsername(person.getUsername()).isPresent()){
            errors.rejectValue("username", "", "Такой логин уже существует");
        }

        if(peopleRepository.findByEmail(person.getEmail()).isPresent()){
            errors.rejectValue("email", "", "Такой email уже существует");
        }
    }

    /**
     * Проверка на уникальность userName и email
     * @param username Логин пользователя
     * @param email Email пользователя
     * @return возвращает список ошибок, если они есть
     */
    public  ErrorResponse validate(String username, String email){
        List<String> error = new ArrayList<>();

        if(peopleRepository.findByUsername(username).isPresent()){
            error.add( "Такой логин уже существует");
        }

        if(peopleRepository.findByEmail(email).isPresent()){
            error.add("Такой email уже существует");
        }

        ErrorResponse errors = new ErrorResponse(error, new Date());

        return errors;
    }

    /**
     * Проверка пароля при входе
     * @param usernameOrEmail логин или email пользователя
     * @param password пароль пользователя
     */
    public void checkPassword(String usernameOrEmail, String password){

        Optional<Person> person = Optional.ofNullable(peopleRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new ElementNotFoundException("Пользователь не найден")));

        if(!passwordEncoder.matches(password, person.get().getPassword())){
            throw new ElementNotFoundException("Пароль не найден");
        }

    }
}
