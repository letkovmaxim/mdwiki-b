package org.sbtitcourses.mdwiki.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.model.StoredFile;
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
 * Тесты для репозитория сущности StoredFile
 */
@DataJpaTest
class StoredFileRepositoryTests {

    private final TestEntityManager entityManager;
    private final StoredFileRepository storedFileRepository;
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
    private final StoredFile storedFile = new StoredFile(
            "75200c8f-cd53-4a91-a1fc-49493f1b1bbe",
            "testName",
            "testType",
            100,
            owner,
            space
    );

    @Autowired
    StoredFileRepositoryTests(TestEntityManager entityManager, StoredFileRepository storedFileRepository) {
        this.entityManager = entityManager;
        this.storedFileRepository = storedFileRepository;
    }

    @BeforeEach
    public void setUp() {
        entityManager.persist(owner);
        entityManager.persist(space);
        entityManager.persistAndFlush(storedFile);
        entityManager.clear();
    }

    @Test
    void findByGuidShouldReturnStoredFile() {
        Optional<StoredFile> found = storedFileRepository.findByGUID("75200c8f-cd53-4a91-a1fc-49493f1b1bbe");

        assertTrue(found.isPresent());
        assertEquals(storedFile.getId(), found.get().getId());
        assertEquals("75200c8f-cd53-4a91-a1fc-49493f1b1bbe", found.get().getGUID());
    }

    @Test
    void findByOwnerShouldReturnStoredFile() {
        Pageable pageable = PageRequest.of(0, 1);
        List<StoredFile> found = storedFileRepository.findByOwner(owner, pageable);

        assertFalse(found.isEmpty());
        assertEquals("75200c8f-cd53-4a91-a1fc-49493f1b1bbe", found.get(0).getGUID());
    }
}