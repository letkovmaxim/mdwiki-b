package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тест для репозитория сущности Document
 */
@DataJpaTest
class DocumentRepositoryTests {

    private final TestEntityManager entityManager;
    private final DocumentRepository documentRepository;
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
    private final Document document = Document.builder()
            .page(page)
            .text("testText")
            .build();

    @Autowired
    DocumentRepositoryTests(TestEntityManager entityManager, DocumentRepository documentRepository) {
        this.entityManager = entityManager;
        this.documentRepository = documentRepository;
    }

    @BeforeEach
    public void setUp() {
        entityManager.persist(owner);
        entityManager.persist(space);
        entityManager.persist(page);
        entityManager.persistAndFlush(document);
        entityManager.clear();
    }

    @Test
    public void findByPageShouldReturnPage() {
        Optional<Document> found = documentRepository.findByPage(page);

        assertTrue(found.isPresent());
        assertEquals(document.getId(), found.get().getId());
    }
}