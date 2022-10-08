package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PersonRepositoryTests {
    
    private final TestEntityManager entityManager;
    private final PersonRepository personRepository;
    private final Person person = new Person("testUsername", "testPassword", "testName","testEmail@test.test");

    @Autowired
    PersonRepositoryTests(TestEntityManager entityManager, PersonRepository personRepository) {
        this.entityManager = entityManager;
        this.personRepository = personRepository;
    }

    @BeforeEach
    void setUp() {
        entityManager.persist(person);
        entityManager.flush();
    }

    @Test
    public void test_username() {
        Person found = personRepository.findByUsername(person.getUsername()).orElse(new Person());

        assertEquals(person.getUsername(), found.getUsername());
    }

    @Test
    public void test_email() {
        Person found = personRepository.findByEmail(person.getEmail()).orElse(new Person());

        assertEquals(person.getEmail(), found.getEmail());
    }

    @Test
    public void test_username_or_email() {
        Person foundByUsername = personRepository.findByUsernameOrEmail(person.getUsername(), "").orElse(new Person());
        Person foundByEmail = personRepository.findByUsernameOrEmail("", person.getEmail()).orElse(new Person());

        assertEquals(person.getUsername(), foundByUsername.getUsername());
        assertEquals(person.getEmail(), foundByEmail.getEmail());
        assertEquals(foundByUsername, foundByEmail);
    }
}