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
import org.sbtitcourses.mdwiki.util.EntityFetcher;
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
    private EntityFetcher entityFetcher;
    @InjectMocks
    private DocumentService documentService;

    private final Person owner = Person.builder().id(1).build();
    private final Person notOwner = Person.builder().id(2).build();
    private final Space space = Space.builder().owner(owner).build();
    private final Page page = Page.builder().space(space).build();
    private final Document document = new Document();
    private final Document documentWithId = Document.builder().id(1).page(page).build();
    private final Document documentToUpdateWith = Document.builder().text("testText").build();

    @Test
    public void createShouldReturnDocument() {
        when(entityFetcher.fetchPage(1, 1)).thenReturn(page);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);
        when(documentRepository.save(document)).thenReturn(documentWithId);

        Document createdDocument = documentService.create(document, 1, 1);

        assertEquals(1, createdDocument.getId());

        verify(entityFetcher).fetchPage(1, 1);
        verify(entityFetcher).getLoggedInUser();
        verify(documentRepository).save(document);
    }

    @Test
    public void createWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchPage(1, 1)).thenReturn(page);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> documentService.create(document, 1, 1));

        verify(entityFetcher).fetchPage(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void getShouldReturnDocument() {
        when(entityFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        Document gottenDocument = documentService.get(1, 1);

        assertEquals(1, gottenDocument.getId());

        verify(entityFetcher).fetchDocument(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void getWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> documentService.get(1, 1));

        verify(entityFetcher).fetchDocument(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void updateShouldReturnDocument() {
        when(entityFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        Document updatedDocument = documentService.update(1, 1, documentToUpdateWith);

        assertEquals("testText", updatedDocument.getText());

        verify(entityFetcher).fetchDocument(1, 1);
        verify(documentRepository).save(documentWithId);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void updateWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class,
                () -> documentService.update(1, 1, documentToUpdateWith));

        verify(entityFetcher).fetchDocument(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void deleteShouldRemoveDocument() {
        when(entityFetcher.fetchDocument(1, 1)).thenReturn(documentWithId)
                .thenThrow(ElementNotFoundException.class);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        assertDoesNotThrow(() -> documentService.delete(1, 1));
        assertThrows(ElementNotFoundException.class, () -> documentService.delete(1, 1));

        verify(entityFetcher, times(2)).fetchDocument(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void deleteWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchDocument(1, 1)).thenReturn(documentWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> documentService.delete(1, 1));

        verify(entityFetcher).fetchDocument(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }
}