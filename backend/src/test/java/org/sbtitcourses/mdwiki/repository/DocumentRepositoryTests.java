package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест для репозитория сущности Document
 */
@DataJpaTest
class DocumentRepositoryTests {

    private final TestEntityManager entityManager;
    private final DocumentRepository documentRepository;
    private final Page page = new Page();
    private final Document document = new Document(page);

    @Autowired
    DocumentRepositoryTests(TestEntityManager entityManager, DocumentRepository documentRepository) {
        this.entityManager = entityManager;
        this.documentRepository = documentRepository;
    }

    @BeforeEach
    public void setUp() {
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