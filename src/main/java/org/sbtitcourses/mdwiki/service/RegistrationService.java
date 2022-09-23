package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        //Зашифрованный пароль
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        person.setRole("ROLE_USER");

        person.setActive(true);

        peopleRepository.save(person);
    }

    public void checkUserName(String userName, Errors errors){
        if(!peopleRepository.findByUsername(userName).isEmpty()){
            errors.rejectValue("username", "", "Такой UserName уже существует");
        }
    }

    public void checkEmail(String email, Errors errors){
        Pattern p = Pattern.compile("\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*\\.\\w{2,4}");
        Matcher m = p.matcher(email);
        if(!m.matches()){
            errors.rejectValue("email", "", "Неккоректно введен email");
        }

        if(!peopleRepository.findByEmail(email).isEmpty()){
            errors.rejectValue("email", "", "Такой email уже существует");
        }
    }

    public void checkPassword(String password, String repeatPassword, Errors errors){
        if(!password.equals(repeatPassword)){
            errors.rejectValue("password", "", "Пароли не совпадают");
        }
    }
}
