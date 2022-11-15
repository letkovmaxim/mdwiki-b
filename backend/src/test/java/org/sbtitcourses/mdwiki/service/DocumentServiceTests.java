package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.DocumentRepository;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тест для сервиса с логикой CRUD операций над сущностью Document
 */
@ExtendWith(MockitoExtension.class)
class DocumentServiceTests {

    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private ResourceFetcher resourceFetcher;
    @InjectMocks
    private DocumentService documentService;

    private final Person owner = new Person(1);
    private final Person notOwner = new Person(2);
    private final Space space = new Space(owner);
    private final Page page = new Page(space);
    private final Document document = new Document();
    private final Document documentWithId = new Document(1, page);
    private final Document documentToUpdateWith = new Document("testText");

    @Test
    public void createShouldReturnDocument() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(page);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);
        when(documentRepository.save(document)).thenReturn(documentWithId);

        Document createdDocument = documentService.create(document, 1, 1);

        assertEquals(1, createdDocument.getId());

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceFetcher).getLoggedInUser();
        verify(documentRepository).save(document);
    }

    @Test
    public void createWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(page);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> documentService.create(document, 1, 1));

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void getShouldReturnDocument() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        Document gottenDocument = documentService.get(1, 1);

        assertEquals(1, gottenDocument.getId());

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void getWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> documentService.get(1, 1));

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void updateShouldReturnDocument() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        Document updatedDocument = documentService.update(1, 1, documentToUpdateWith);

        assertEquals("testText", updatedDocument.getText());

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(documentRepository).save(documentWithId);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void updateWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class,
                () -> documentService.update(1, 1, documentToUpdateWith));

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void deleteShouldRemoveDocument() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(documentWithId)
                .thenThrow(ElementNotFoundException.class);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        assertDoesNotThrow(() -> documentService.delete(1, 1));
        assertThrows(ElementNotFoundException.class, () -> documentService.delete(1, 1));

        verify(resourceFetcher, times(2)).fetchDocument(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void deleteWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> documentService.delete(1, 1));

        verify(resourceFetcher).fetchDocument(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }
}