package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.util.EntityFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тест для сервиса с логикой CRUD операций над сущностью Page
 */
@ExtendWith({MockitoExtension.class})
class PageServiceTests {

    @Mock
    private PageRepository pageRepository;
    @Mock
    private EntityFetcher entityFetcher;
    @InjectMocks
    private PageService pageService;
    private final Person owner = Person.builder().id(1).build();
    private final Person notOwner = Person.builder().id(2).build();
    private final Space space = Space.builder().owner(owner).build();
    private final Page pageToCreate = new Page();
    private final Page pageWithId = Page.builder()
            .id(1)
            .name("pageName")
            .space(space)
            .build();
    private final Page parentPage = Page.builder()
            .id(2)
            .space(space)
            .build();
    private final Page pageToUpdateWith = Page.builder()
            .name("testName")
            .shared(true)
            .build();

    @Test
    public void createShouldReturnPage() {
        when(entityFetcher.fetchSpace(1)).thenReturn(space);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);
        when(pageRepository.save(pageToCreate)).thenReturn(pageWithId);

        Page createdPage = pageService.create(pageToCreate, 1);

        assertEquals(1, createdPage.getId());

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
        verify(pageRepository).save(pageToCreate);
    }

    @Test
    public void createWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchSpace(1)).thenReturn(space);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.create(pageToCreate, 1));

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void createSubpageShouldReturnPage() {
        when(entityFetcher.fetchPage(2, 1)).thenReturn(parentPage);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);
        when(pageRepository.save(pageToCreate)).thenReturn(pageWithId);

        Page createdSubpage = pageService.createSubpage(pageToCreate, 2, 1);

        assertEquals(1, createdSubpage.getId());

        verify(entityFetcher).fetchPage(2, 1);
        verify(entityFetcher).getLoggedInUser();
        verify(pageRepository).save(pageToCreate);
    }

    @Test
    public void createSubpageWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchPage(2, 1)).thenReturn(parentPage);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.createSubpage(pageToCreate, 2, 1));

        verify(entityFetcher).fetchPage(2, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void getAllShouldReturnPageList() {
        List<Page> pages = new LinkedList<>();
        pages.add(pageWithId);
        pages.add(new Page());
        Pageable pageable = PageRequest.of(0,1);

        when(entityFetcher.fetchSpace(1)).thenReturn(space);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);
        when(pageRepository.findBySpaceAndParentIsNullOrderById(space, pageable)).thenReturn(pages);

        List<Page> gottenPages = pageService.get(1, 0, 1);

        assertEquals(2, gottenPages.size());
        assertEquals(1, gottenPages.get(0).getId());

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
        verify(pageRepository).findBySpaceAndParentIsNullOrderById(space, pageable);
    }

    @Test
    public void getAllWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchSpace(1)).thenReturn(space);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.get(1, 0, 1));

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void getShouldReturnPage() {
        when(entityFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        Page gottenPage = pageService.get(1, 1);

        assertEquals(1, gottenPage.getId());

        verify(entityFetcher).fetchPage(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void getWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.get(1, 1));

        verify(entityFetcher).fetchPage(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void updateShouldReturnPage() {
        when(entityFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        Page updatedPage = pageService.update(1, 1, pageToUpdateWith);

        assertEquals(1, updatedPage.getId());
        assertEquals(pageToUpdateWith.getName(), updatedPage.getName());
        assertEquals(pageToUpdateWith.isShared(), updatedPage.isShared());

        verify(entityFetcher).fetchPage(1, 1);
        verify(entityFetcher).getLoggedInUser();
        verify(pageRepository).save(pageWithId);
    }

    @Test
    public void updateWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.update(1, 1, pageToUpdateWith));

        verify(entityFetcher).fetchPage(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void deleteShouldRemovePage() {
        when(entityFetcher.fetchPage(1, 1))
                .thenReturn(pageWithId)
                .thenThrow(ElementNotFoundException.class);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        assertDoesNotThrow(() -> pageService.delete(1, 1));
        assertThrows(ElementNotFoundException.class, () -> pageService.delete(1, 1));

        verify(entityFetcher, times(2)).fetchPage(1, 1);
        verify(entityFetcher).getLoggedInUser();
        verify(pageRepository).delete(pageWithId);
    }

    @Test
    public void deleteWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.delete(1, 1));

        verify(entityFetcher).fetchPage(1, 1);
        verify(entityFetcher).getLoggedInUser();
    }
}