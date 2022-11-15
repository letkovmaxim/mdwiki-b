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
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
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
    private ResourceFetcher resourceFetcher;
    @InjectMocks
    private PageService pageService;
    private final Person owner = new Person(1);
    private final Person notOwner = new Person(2);
    private final Space space = new Space(owner);
    private final Page pageToCreate = new Page();
    private final Page pageWithId = new Page(1, space);
    private final Page parentPage = new Page(2, space);
    private final Page pageToUpdateWith = new Page("testName", true);

    @Test
    public void createShouldReturnPage() {
        when(resourceFetcher.fetchSpace(1)).thenReturn(space);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);
        when(pageRepository.save(pageToCreate)).thenReturn(pageWithId);

        Page createdPage = pageService.create(pageToCreate, 1);

        assertEquals(1, createdPage.getId());

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
        verify(pageRepository).save(pageToCreate);
    }

    @Test
    public void createWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchSpace(1)).thenReturn(space);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.create(pageToCreate, 1));

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void createSubpageShouldReturnPage() {
        when(resourceFetcher.fetchPage(2, 1)).thenReturn(parentPage);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);
        when(pageRepository.save(pageToCreate)).thenReturn(pageWithId);

        Page createdSubpage = pageService.createSubpage(pageToCreate, 2, 1);

        assertEquals(1, createdSubpage.getId());

        verify(resourceFetcher).fetchPage(2, 1);
        verify(resourceFetcher).getLoggedInUser();
        verify(pageRepository).save(pageToCreate);
    }

    @Test
    public void createSubpageWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchPage(2, 1)).thenReturn(parentPage);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.createSubpage(pageToCreate, 2, 1));

        verify(resourceFetcher).fetchPage(2, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void getAllShouldReturnPageList() {
        List<Page> pages = new LinkedList<>();
        pages.add(pageWithId);
        pages.add(new Page());
        Pageable pageable = PageRequest.of(0,1);

        when(resourceFetcher.fetchSpace(1)).thenReturn(space);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);
        when(pageRepository.findBySpaceAndParentIsNull(space, pageable)).thenReturn(pages);

        List<Page> gottenPages = pageService.get(1, 0, 1);

        assertEquals(2, gottenPages.size());
        assertEquals(1, gottenPages.get(0).getId());

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
        verify(pageRepository).findBySpaceAndParentIsNull(space, pageable);
    }

    @Test
    public void getAllWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchSpace(1)).thenReturn(space);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.get(1, 0, 1));

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void getShouldReturnPage() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        Page gottenPage = pageService.get(1, 1);

        assertEquals(1, gottenPage.getId());

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void getWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.get(1, 1));

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void updateShouldReturnPage() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        Page updatedPage = pageService.update(1, 1, pageToUpdateWith);

        assertEquals(1, updatedPage.getId());
        assertEquals(pageToUpdateWith.getName(), updatedPage.getName());
        assertEquals(pageToUpdateWith.isShared(), updatedPage.isShared());

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceFetcher).getLoggedInUser();
        verify(pageRepository).save(pageWithId);
    }

    @Test
    public void updateWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.update(1, 1, pageToUpdateWith));

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void deleteShouldRemovePage() {
        when(resourceFetcher.fetchPage(1, 1))
                .thenReturn(pageWithId)
                .thenThrow(ElementNotFoundException.class);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        assertDoesNotThrow(() -> pageService.delete(1, 1));
        assertThrows(ElementNotFoundException.class, () -> pageService.delete(1, 1));

        verify(resourceFetcher, times(2)).fetchPage(1, 1);
        verify(resourceFetcher).getLoggedInUser();
        verify(pageRepository).delete(pageWithId);
    }

    @Test
    public void deleteWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchPage(1, 1)).thenReturn(pageWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> pageService.delete(1, 1));

        verify(resourceFetcher).fetchPage(1, 1);
        verify(resourceFetcher).getLoggedInUser();
    }
}