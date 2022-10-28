package org.sbtitcourses.mdwiki.controller;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.service.RegistrationService;
import org.sbtitcourses.mdwiki.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;

/**
 * Контроллер для страниц login и registration
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    /**
     * Сервис для завершения регистрации пользователя
     */
    private final RegistrationService registrationService;

    /**
     * Валидатор, который проверяет введенные поля пользоватлем
     */
    private final PersonValidator personValidator;

    /**
     * Инициализация полей класса
     */
    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }

    /**
     * @return возвращает страницу для входа пользователя
     */
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    /**
     * @param person в нем будут хранить все данные введенные пользователем
     * @return возвращает страницу для регистрации пользователя
     */
    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }


    /**
     * Оболочка для проверки полей ввода при регистрации
     * При помощи validate проверяются все поля класса Person
     * Т.к. repeatPassword не является полем класса Person, поэтому был создан отдельный метод checkPassword для проаерки password(из Person) и repeatPassword
     * @param person содержит данные введенные пользователем при регистрации
     * @param bindingResult содержит ошибки допущенные пользоватлем при регистрации
     * @param repeatPassword поле для подтверждения пароля при регистрации
     * @return страницу для входа, елси нет ошибок, в противном случае страницу регистрации
     */
    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person,
                                      BindingResult bindingResult, String repeatPassword) {
        personValidator.validate(person, bindingResult);
        personValidator.checkPassword(person.getPassword(), repeatPassword, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        registrationService.register(person);

        return "redirect:/auth/login";
    }
}
