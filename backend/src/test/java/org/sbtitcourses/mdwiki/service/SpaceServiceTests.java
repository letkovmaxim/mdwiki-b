package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Executable;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тест для сервиса с логикой CRUD операций над сущностью Space
 */
@ExtendWith(MockitoExtension.class)
class SpaceServiceTests {

    @Mock
    private SpaceRepository spaceRepository;
    @Mock
    private ResourceFetcher resourceFetcher;
    @InjectMocks
    private SpaceService spaceService;
    private final Person owner = new Person(1);
    private final Person notOwner = new Person(2);
    private final Space spaceToCreate = new Space();
    private final Space spaceWithId = new Space(1, owner);
    private final Space spaceToUpdateWith = new Space("testName", true);

    @Test
    public void createShouldReturnSpace() {
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);
        when(spaceRepository.save(spaceToCreate)).thenReturn(spaceWithId);

        Space createdSpace = spaceService.create(spaceToCreate);

        assertEquals(1, createdSpace.getId());

        verify(resourceFetcher).getLoggedInUser();
        verify(spaceRepository).save(spaceToCreate);
    }

    @Test
    public void getAllShouldReturnSpaceList() {
        List<Space> spaces = new LinkedList<>();
        spaces.add(spaceWithId);
        Pageable pageable = PageRequest.of(0, 1);

        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);
        when(spaceRepository.findByOwnerOrSharedTrue(owner, pageable)).thenReturn(spaces);

        List<Space> gottenSpaces = spaceService.get(0, 1);

        assertEquals(1, gottenSpaces.size());
        assertEquals(1, gottenSpaces.get(0).getId());

        verify(resourceFetcher).getLoggedInUser();
        verify(spaceRepository).findByOwnerOrSharedTrue(owner, pageable);
    }

    @Test
    public void getShouldReturnSpace() {
        when(resourceFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        Space gottenSpace = spaceService.get(1);

        assertEquals(1, gottenSpace.getId());

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void getWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> spaceService.get(1));

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void updateShouldReturnSpace() {
        when(resourceFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        Space updatedSpace = spaceService.update(1, spaceToUpdateWith);

        assertEquals(1, updatedSpace.getId());
        assertEquals(spaceToUpdateWith.getName(), updatedSpace.getName());
        assertEquals(spaceToUpdateWith.isShared(), updatedSpace.isShared());

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void updateWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> spaceService.update(1, spaceToUpdateWith));

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void deleteShouldRemoveSpace() {
        when(resourceFetcher.fetchSpace(1))
                .thenReturn(spaceWithId)
                .thenThrow(ElementNotFoundException.class);
        when(resourceFetcher.getLoggedInUser()).thenReturn(owner);

        assertDoesNotThrow(() -> spaceService.delete(1));
        assertThrows(ElementNotFoundException.class, () -> spaceService.delete(1));

        verify(resourceFetcher, times(2)).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
    }

    @Test
    public void deleteWithAccessDeniedShouldThrowException() {
        when(resourceFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(resourceFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> spaceService.delete(1));

        verify(resourceFetcher).fetchSpace(1);
        verify(resourceFetcher).getLoggedInUser();
    }
}