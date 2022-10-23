package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.util.exception.PageNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тест для сервиса с логикой CRUD операций над сущностью Page
 */
@ExtendWith({MockitoExtension.class})
class PageServiceTests {

    @Mock
    private PageRepository pageRepository;
    @InjectMocks
    private PageService pageService;
    private final Space space;
    private final Space wrongSpace;
    private final Page pageToCreate;
    private final Page pageWithId;
    private final Page pageToUpdateWith;

    {
        space = new Space();

        wrongSpace = new Space();

        pageToCreate = new Page();
        pageToCreate.setPublic(false);

        pageWithId = new Page();
        pageWithId.setId(1);
        pageWithId.setSpace(space);

        pageToUpdateWith = new Page();
        pageToUpdateWith.setName("testName");
        pageToUpdateWith.setPublic(true);
    }

    @Test
    public void createShouldReturnPage() {
        when(pageRepository.save(pageToCreate)).thenReturn(pageWithId);

        Page createdPage = pageService.create(pageToCreate);

        assertEquals(1, createdPage.getId());
        assertFalse(createdPage.getPublic());
        verify(pageRepository).save(pageToCreate);
    }

    @Test
    public void getAllShouldReturnPageList() {
        List<Page> pages = new LinkedList<>();
        pages.add(pageWithId);
        pages.add(new Page());
        Pageable pageable = PageRequest.of(0,1);

        when(pageRepository.findBySpaceAndParentIsNull(space, pageable)).thenReturn(pages);

        List<Page> gottenPages = pageService.getAll(space, 0, 1);

        assertEquals(pages.size(), gottenPages.size());
        assertEquals(1, gottenPages.get(0).getId());
        verify(pageRepository).findBySpaceAndParentIsNull(space, pageable);
    }

    @Test
    public void getShouldReturnPage() {
        when(pageRepository.findByIdAndSpace(1, space)).thenReturn(Optional.of(pageWithId));
        when(pageRepository.findByIdAndSpace(2, space)).thenReturn(Optional.empty());
        when(pageRepository.findByIdAndSpace(1, wrongSpace)).thenReturn(Optional.empty());

        Page gottenPage = pageService.get(1, space);

        assertEquals(1, gottenPage.getId());
        assertThrows(PageNotFoundException.class, () -> pageService.get(2, space));
        assertThrows(PageNotFoundException.class, () -> pageService.get(1, wrongSpace));
        verify(pageRepository).findByIdAndSpace(1, space);
        verify(pageRepository).findByIdAndSpace(2, space);
        verify(pageRepository).findByIdAndSpace(1, wrongSpace);
    }

    @Test
    public void updateShouldReturnPage() {
        when(pageRepository.findByIdAndSpace(1, space)).thenReturn(Optional.of(pageWithId));
        when(pageRepository.findByIdAndSpace(2, space)).thenReturn(Optional.empty());
        when(pageRepository.findByIdAndSpace(1, wrongSpace)).thenReturn(Optional.empty());

        Page updatedPage = pageService.update(1, space, pageToUpdateWith);

        assertEquals(1, updatedPage.getId());
        assertEquals(pageToUpdateWith.getName(), updatedPage.getName());
        assertEquals(pageToUpdateWith.getPublic(), updatedPage.getPublic());
        assertThrows(PageNotFoundException.class, () -> pageService.update(2, space, pageToUpdateWith));
        assertThrows(PageNotFoundException.class, () -> pageService.update(1, wrongSpace, pageToUpdateWith));
        verify(pageRepository).findByIdAndSpace(1, space);
        verify(pageRepository).save(pageWithId);
        verify(pageRepository).findByIdAndSpace(2, space);
        verify(pageRepository).findByIdAndSpace(1, wrongSpace);
    }

    @Test
    public void deleteShouldRemovePage() {
        when(pageRepository.findById(1)).thenReturn(Optional.of(pageWithId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> pageService.delete(1));
        assertThrows(PageNotFoundException.class, () -> pageService.delete(1));
        verify(pageRepository, times(2)).findById(1);
        verify(pageRepository).delete(pageWithId);
    }
}