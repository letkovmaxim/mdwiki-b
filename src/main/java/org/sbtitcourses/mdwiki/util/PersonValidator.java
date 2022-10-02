package org.sbtitcourses.mdwiki.util;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
     * Инициализая поля
     */
    @Autowired
    public PersonValidator(PersonRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
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
     * Проверка на совпадение password и repeatPassword
     * @param password ввод пароля пользователем
     * @param repeatPassword подтверждение пароля
     * @param errors если они не совпадают, то добавляется ошибка
     */
    public void checkPassword(String password, String repeatPassword, Errors errors){
        if(!password.equals(repeatPassword)){
            errors.rejectValue("password", "", "Пароли не совпадают");
        }
    }
}
