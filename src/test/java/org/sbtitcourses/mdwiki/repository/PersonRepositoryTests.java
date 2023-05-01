package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тест для репозитория сущности Person
 */
@DataJpaTest
class PersonRepositoryTests {
    
    private final TestEntityManager entityManager;
    private final PersonRepository personRepository;
    private final Person person = Person.builder()
            .username("testUsername")
            .password("testPassword")
            .name("testName")
            .email("testEmail@test.test")
            .role("ROLE_USER")
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

    @Autowired
    PersonRepositoryTests(TestEntityManager entityManager, PersonRepository personRepository) {
        this.entityManager = entityManager;
        this.personRepository = personRepository;
    }

    @BeforeEach
    public void setUp() {
        entityManager.persistAndFlush(person);
        entityManager.clear();
    }

    @Test
    public void findByUsernameShouldReturnPerson() {
        Optional<Person> found = personRepository.findByUsername(person.getUsername());

        assertTrue(found.isPresent());
        assertEquals(person.getId(), found.get().getId());
    }

    @Test
    public void findByEmailShouldReturnPerson() {
        Optional<Person> found = personRepository.findByEmail(person.getEmail());

        assertTrue(found.isPresent());
        assertEquals(person.getId(), found.get().getId());
    }

    @Test
    public void findByUsernameOrEmailShouldReturnPerson() {
        Optional<Person> foundByUsername = personRepository.findByUsernameOrEmail(person.getUsername(), "");
        Optional<Person> foundByEmail = personRepository.findByUsernameOrEmail("", person.getEmail());

        assertTrue(foundByUsername.isPresent() && foundByEmail.isPresent());
        assertEquals(person.getId(), foundByUsername.get().getId());
        assertEquals(person.getId(), foundByEmail.get().getId());
    }
}