package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.DocumentRepository;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тест для сервиса с логикой CRUD операций над сущностью Document
 */
@ExtendWith(MockitoExtension.class)
class DocumentServiceTests {
    ;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private ResourceFetcher resourceFetcher;
    @Mock
    private ResourceAccessHelper resourceAccessHelper;
    @InjectMocks
    private DocumentService documentService;

    private final Page page = new Page();
    private final Document document = new Document();
    private final Document documentWithId = new Document(1, page);
    private final Document documentToUpdateWith = new Document("testText");

    @Test
    public void createShouldReturnDocument() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(page);
        when(resourceAccessHelper.isAccessToCreateDocumentDenied(page)).thenReturn(false);
        when(documentRepository.save(document)).thenReturn(documentWithId);

        Document createdDocument = documentService.create(document, 1, 1);

        assertEquals(1, createdDocument.getId());

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceAccessHelper).isAccessToCreateDocumentDenied(page);
        verify(documentRepository).save(document);
    }

    @Test
    public void createWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(page);
        when(resourceAccessHelper.isAccessToCreateDocumentDenied(page))
                .thenThrow(new AccessDeniedException("Доступ запрещен"));

        assertThrows(AccessDeniedException.class, () -> documentService.create(document, 1, 1));

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceAccessHelper).isAccessToCreateDocumentDenied(page);
    }

    @Test
    public void getShouldReturnDocument() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(resourceAccessHelper.isAccessToReadDocumentDenied(documentWithId)).thenReturn(false);

        Document gottenDocument = documentService.get(1, 1);

        assertEquals(1, gottenDocument.getId());

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(resourceAccessHelper).isAccessToReadDocumentDenied(documentWithId);
    }

    @Test
    public void getWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(document);
        when(resourceAccessHelper.isAccessToReadDocumentDenied(document))
                .thenThrow(new AccessDeniedException("Доступ запрещен"));

        assertThrows(AccessDeniedException.class, () -> documentService.get(1, 1));

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(resourceAccessHelper).isAccessToReadDocumentDenied(document);
    }

    @Test
    public void updateShouldReturnDocument() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(document);
        when(resourceAccessHelper.isAccessToUpdateDocumentDenied(document)).thenReturn(false);

        Document updatedDocument = documentService.update(1, 1, documentToUpdateWith);

        assertEquals("testText", updatedDocument.getText());

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(documentRepository).save(document);
        verify(resourceAccessHelper).isAccessToUpdateDocumentDenied(document);
    }

    @Test
    public void updateWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(document);
        when(resourceAccessHelper.isAccessToUpdateDocumentDenied(document))
                .thenThrow(new AccessDeniedException("Доступ запрещен"));

        assertThrows(AccessDeniedException.class,
                () -> documentService.update(1, 1, documentToUpdateWith));

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(resourceAccessHelper).isAccessToUpdateDocumentDenied(document);
    }

    @Test
    public void deleteShouldRemoveDocument() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(documentWithId)
                .thenThrow(new ElementNotFoundException("Документ не найден"));
        when(resourceAccessHelper.isAccessToDeleteDocumentDenied(documentWithId)).thenReturn(false);

        assertDoesNotThrow(() -> documentService.delete(1, 1));
        assertThrows(ElementNotFoundException.class, () -> documentService.delete(1, 1));

        verify(resourceFetcher, times(2)).fetchDocument(1, 1);
        verify(resourceAccessHelper).isAccessToDeleteDocumentDenied(documentWithId);
    }

    @Test
    public void deleteWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(document);
        when(resourceAccessHelper.isAccessToDeleteDocumentDenied(document))
                .thenThrow(new AccessDeniedException("Доступ запрещен"));

        assertThrows(AccessDeniedException.class, () -> documentService.delete(1, 1));

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(resourceAccessHelper).isAccessToDeleteDocumentDenied(document);
    }
}