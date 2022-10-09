package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
    private final Page page = new Page("testName", true);

    @Autowired
    PageRepositoryTests(TestEntityManager entityManager, PageRepository pageRepository) {
        this.entityManager = entityManager;
        this.pageRepository = pageRepository;
    }

    @BeforeEach
    public void setUp() {
        entityManager.persistAndFlush(page);
        entityManager.clear();
    }

    @Test
    public void whenFindByName_thenReturnPage() {
        Optional<Page> found = pageRepository.findByName(page.getName());

        assertTrue(found.isPresent());
        assertEquals(page.getId(), found.get().getId());
    }

    @Test
    public void whenFindByIsPublicTrue_thenReturnPage() {
        List<Page> found = pageRepository.findByIsPublicTrue();

        assertFalse(found.isEmpty());
        assertEquals(page.getId(), found.get(0).getId());
    }
}