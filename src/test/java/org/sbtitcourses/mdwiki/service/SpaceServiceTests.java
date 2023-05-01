package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
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
 * Тест для сервиса с логикой CRUD операций над сущностью Space
 */
@ExtendWith(MockitoExtension.class)
class SpaceServiceTests {

    @Mock
    private SpaceRepository spaceRepository;
    @Mock
    private EntityFetcher entityFetcher;
    @InjectMocks
    private SpaceService spaceService;
    private final Person owner = Person.builder().id(1).build();
    private final Person notOwner = Person.builder().id(2).build();
    private final Space spaceToCreate = new Space();
    private final Space spaceWithId = Space.builder()
            .id(1)
            .name("spaceName")
            .owner(owner)
            .build();
    private final Space spaceToUpdateWith = Space.builder()
            .name("testName")
            .shared(true)
            .build();

    @Test
    public void createShouldReturnSpace() {
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);
        when(spaceRepository.save(spaceToCreate)).thenReturn(spaceWithId);

        Space createdSpace = spaceService.create(spaceToCreate);

        assertEquals(1, createdSpace.getId());

        verify(entityFetcher).getLoggedInUser();
        verify(spaceRepository).save(spaceToCreate);
    }

    @Test
    public void getAllShouldReturnSpaceList() {
        List<Space> spaces = new LinkedList<>();
        spaces.add(spaceWithId);
        Pageable pageable = PageRequest.of(0, 1);

        when(entityFetcher.getLoggedInUser()).thenReturn(owner);
        when(spaceRepository.findByOwnerOrderById(owner, pageable)).thenReturn(spaces);

        List<Space> gottenSpaces = spaceService.get(0, 1);

        assertEquals(1, gottenSpaces.size());
        assertEquals(1, gottenSpaces.get(0).getId());

        verify(entityFetcher).getLoggedInUser();
        verify(spaceRepository).findByOwnerOrderById(owner, pageable);
    }

    @Test
    public void getSharedShouldReturnSpaceList() {
        List<Space> spaces = new LinkedList<>();
        spaces.add(spaceWithId);
        Pageable pageable = PageRequest.of(0, 1);

        when(spaceRepository.findBySharedTrue(pageable)).thenReturn(spaces);

        List<Space> gottenSpaces = spaceService.getShared(0, 1);

        assertEquals(1, gottenSpaces.size());
        assertEquals(1, gottenSpaces.get(0).getId());

        verify(spaceRepository).findBySharedTrue(pageable);
    }

    @Test
    public void getShouldReturnSpace() {
        when(entityFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        Space gottenSpace = spaceService.get(1);

        assertEquals(1, gottenSpace.getId());

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void getWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> spaceService.get(1));

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void updateShouldReturnSpace() {
        when(entityFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        Space updatedSpace = spaceService.update(1, spaceToUpdateWith);

        assertEquals(1, updatedSpace.getId());
        assertEquals(spaceToUpdateWith.getName(), updatedSpace.getName());
        assertEquals(spaceToUpdateWith.isShared(), updatedSpace.isShared());

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void updateWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> spaceService.update(1, spaceToUpdateWith));

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void deleteShouldRemoveSpace() {
        when(entityFetcher.fetchSpace(1))
                .thenReturn(spaceWithId)
                .thenThrow(ElementNotFoundException.class);
        when(entityFetcher.getLoggedInUser()).thenReturn(owner);

        assertDoesNotThrow(() -> spaceService.delete(1));
        assertThrows(ElementNotFoundException.class, () -> spaceService.delete(1));

        verify(entityFetcher, times(2)).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
    }

    @Test
    public void deleteWithAccessDeniedShouldThrowException() {
        when(entityFetcher.fetchSpace(1)).thenReturn(spaceWithId);
        when(entityFetcher.getLoggedInUser()).thenReturn(notOwner);

        assertThrows(AccessDeniedException.class, () -> spaceService.delete(1));

        verify(entityFetcher).fetchSpace(1);
        verify(entityFetcher).getLoggedInUser();
    }
}