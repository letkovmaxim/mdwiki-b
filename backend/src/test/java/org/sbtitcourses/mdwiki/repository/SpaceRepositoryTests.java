package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест для репозитория сущности Space
 */
@DataJpaTest
class SpaceRepositoryTests {

    private final TestEntityManager entityManager;
    private final SpaceRepository spaceRepository;
    private final Space space;
    private final Person owner;

    {
        owner = new Person();
        owner.setUsername("testUsername");
        owner.setName("testName");
        owner.setPassword("testPassword");
        owner.setEmail("testEmail@test.test");

        space = new Space();
        space.setName("testName");
        space.setOwner(owner);
        space.setPublic(true);
    }

    @Autowired
    public SpaceRepositoryTests(TestEntityManager entityManager, SpaceRepository spaceRepository) {
        this.entityManager = entityManager;
        this.spaceRepository = spaceRepository;
    }

    @BeforeEach
    public void setUp() {
        entityManager.persist(owner);
        entityManager.persistAndFlush(space);
        entityManager.clear();
    }

    @Test
    public void findByNameShouldReturnSpace() {
        Optional<Space> found = spaceRepository.findByName(space.getName());

        assertTrue(found.isPresent());
        assertEquals(space.getId(), found.get().getId());
    }

    @Test
    public void findByIsPublicTrueShouldReturnSpaceList() {
        List<Space> found = spaceRepository.findByIsPublicTrue();

        assertFalse(found.isEmpty());
        assertEquals(space.getId(), found.get(0).getId());
    }

    @Test
    public void findByOwnerShouldReturnSpaceList() {
        List<Space> found = spaceRepository.findByOwner(owner);

        assertFalse(found.isEmpty());
        assertEquals(space.getId(), found.get(0).getId());
        assertEquals(owner.getId(), found.get(0).getOwner().getId());
    }
}