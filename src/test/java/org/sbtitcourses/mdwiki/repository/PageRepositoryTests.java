package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Page;
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
 * Тест для репозитория сущности Page
 */
@DataJpaTest
class PageRepositoryTests {

    private final TestEntityManager entityManager;
    private final PageRepository pageRepository;
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
    private final Page page = Page.builder()
            .name("testName")
            .space(space)
            .shared(true)
            .createdAt(Instant.now())
            .updatedAt(Instant.now())
            .build();

    @Autowired
    PageRepositoryTests(TestEntityManager entityManager, PageRepository pageRepository) {
        this.entityManager = entityManager;
        this.pageRepository = pageRepository;
    }

    @BeforeEach
    public void setUp() {
        entityManager.persist(owner);
        entityManager.persist(space);
        entityManager.persistAndFlush(page);
        entityManager.clear();
    }

    @Test
    public void findByNameShouldReturnPage() {
        Optional<Page> found = pageRepository.findByName(page.getName());

        assertTrue(found.isPresent());
        assertEquals(page.getId(), found.get().getId());
    }

    @Test
    public void findBySharedTrueShouldReturnPageList() {
        List<Page> found = pageRepository.findBySharedTrue();

        assertFalse(found.isEmpty());
        assertEquals(page.getId(), found.get(0).getId());
    }

    @Test
    public void findByIdAndSpaceShouldReturnPage() {
        Optional<Page> found = pageRepository.findByIdAndSpace(page.getId(), space);

        assertTrue(found.isPresent());
        assertEquals(page.getId(), found.get().getId());
        assertEquals(space.getId(), found.get().getSpace().getId());
    }

    @Test
    public void findBySpaceAndParentIsNullShouldReturnPageList() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Page> found = pageRepository.findBySpaceAndParentIsNullOrderById(space, pageable);

        assertFalse(found.isEmpty());
        assertEquals(page.getId(), found.get(0).getId());
        assertNull(found.get(0).getParent());
    }
}