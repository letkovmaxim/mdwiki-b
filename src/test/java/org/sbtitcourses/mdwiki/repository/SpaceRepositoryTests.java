package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
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
    private final Person owner = Person.builder()
            .username("testUsername")
            .password("testPassword")
            .name("testName")
            .email("testEmail@test.test")
            .role("ROLE_USER")
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();
    private final Space space = Space.builder()
            .name("testName")
            .owner(owner)
            .shared(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

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
    public void findBySharedTrueShouldReturnSpaceList() {
        List<Space> found = spaceRepository.findBySharedTrue(PageRequest.of(0, 1));

        assertFalse(found.isEmpty());
        assertEquals(space.getId(), found.get(0).getId());
    }

    @Test
    public void findByOwnerShouldReturnSpaceList() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Space> found = spaceRepository.findByOwnerOrderById(owner, pageable);

        assertFalse(found.isEmpty());
        assertEquals(space.getId(), found.get(0).getId());
        assertEquals(owner.getId(), found.get(0).getOwner().getId());
    }
}