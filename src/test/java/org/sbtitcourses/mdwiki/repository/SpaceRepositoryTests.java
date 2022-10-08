package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class SpaceRepositoryTests {

    private final TestEntityManager entityManager;
    private final SpaceRepository spaceRepository;
    private final Space space = new Space("testName", true);

    @Autowired
    public SpaceRepositoryTests(TestEntityManager entityManager, SpaceRepository spaceRepository) {
        this.entityManager = entityManager;
        this.spaceRepository = spaceRepository;
    }

    @BeforeEach
    void setUp() {
        entityManager.persist(space);
        entityManager.flush();
    }

    @Test
    public void test_name() {
        Space found = spaceRepository.findByName(space.getName()).orElse(new Space());

        assertEquals(space.getName(), found.getName());
    }

    @Test
    public void test_public() {
        List<Space> found = spaceRepository.findByIsPublicTrue();

        assertEquals(space, found.get(0));
    }

}