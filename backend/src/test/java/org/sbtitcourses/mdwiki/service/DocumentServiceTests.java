package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.repository.DocumentRepository;
import org.sbtitcourses.mdwiki.util.exception.DocumentNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тест для сервиса с логикой CRUD операций над сущностью Document
 */
@ExtendWith(MockitoExtension.class)
class DocumentServiceTests {

    @Mock
    private DocumentRepository documentRepository;
    @InjectMocks
    private DocumentService documentService;
    private final Page page;
    private final Page wrongPage;
    private final Document documentToCreate;
    private final Document documentWithId;
    private final Document documentToUpdateWith;

    {
        page = new Page();

        wrongPage = new Page();

        documentToCreate = new Document();

        documentWithId = new Document();
        documentWithId.setId(1);
        documentWithId.setPage(page);

        documentToUpdateWith = new Document();
        documentToUpdateWith.setText("testText");
    }

    @Test
    public void createShouldReturnDocument() {
        when(documentRepository.save(documentToCreate)).thenReturn(documentWithId);

        Document createdDocument = documentService.create(documentToCreate);

        assertEquals(1, createdDocument.getId());
        verify(documentRepository).save(documentToCreate);
    }

    @Test
    public void getShouldReturnDocument() {
        when(documentRepository.findByPage(page)).thenReturn(Optional.of(documentWithId));
        when(documentRepository.findByPage(wrongPage)).thenReturn(Optional.empty());

        Document gottenDocument = documentService.get(page);

        assertEquals(1, gottenDocument.getId());
        assertThrows(DocumentNotFoundException.class, () -> documentService.get(wrongPage));
        verify(documentRepository).findByPage(page);
        verify(documentRepository).findByPage(wrongPage);
    }

    @Test
    public void updateShouldReturnDocument() {
        when(documentRepository.findByPage(page)).thenReturn(Optional.of(documentWithId));
        when(documentRepository.findByPage(wrongPage)).thenReturn(Optional.empty());

        Document updatedDocument = documentService.update(page, documentToUpdateWith);

        assertEquals(1, updatedDocument.getId());
        assertEquals(documentToUpdateWith.getText(), updatedDocument.getText());
        assertThrows(DocumentNotFoundException.class, () -> documentService.update(wrongPage, documentToUpdateWith));
        verify(documentRepository).findByPage(page);
        verify(documentRepository).save(documentWithId);
        verify(documentRepository).findByPage(wrongPage);
    }

    @Test
    public void deleteShouldRemoveDocument() {
        when(documentRepository.findByPage(page)).thenReturn(Optional.of(documentWithId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> documentService.delete(page));
        assertThrows(DocumentNotFoundException.class, () -> documentService.delete(page));
        verify(documentRepository, times(2)).findByPage(page);
        verify(documentRepository).delete(documentWithId);
    }
}