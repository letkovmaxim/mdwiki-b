package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.PeopleRepository;
import org.sbtitcourses.mdwiki.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService { //Сервис для Spring Security, загружает пользователя
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUsername(usernameOrEmail);

        if (person.isEmpty())
            person = peopleRepository.findByEmail(usernameOrEmail);

        if (person.isEmpty())
            throw new UsernameNotFoundException("User not found in the database");

        return new PersonDetails(person.get());
    }
}
