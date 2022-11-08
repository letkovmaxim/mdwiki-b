package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;

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
    @InjectMocks
    private SpaceService spaceService;
    private final Space spaceToCreate = new Space(false);
    private final Space spaceWithId = new Space(1);
    private final Space spaceToUpdateWith = new Space("testName", true);

    @Test
    public void createShouldReturnSpace() {
        when(spaceRepository.save(spaceToCreate)).thenReturn(spaceWithId);

        Space createdSpace = spaceService.create(spaceToCreate);

        assertEquals(1, createdSpace.getId());
        assertFalse(createdSpace.isShared());
        verify(spaceRepository).save(spaceToCreate);
    }

    @Test
    public void getAllShouldReturnSpaceList() {
        List<Space> spaces = new LinkedList<>();
        spaces.add(spaceWithId);

        when(spaceRepository.findAll()).thenReturn(spaces);

        List<Space> gottenSpaces = spaceService.get(0, 1);

        assertEquals(spaces.size(), gottenSpaces.size());
        assertEquals(1, gottenSpaces.get(0).getId());
        verify(spaceRepository).findAll();
    }

    @Test
    public void getShouldReturnSpace() {
        when(spaceRepository.findById(1)).thenReturn(Optional.of(spaceWithId));
        when(spaceRepository.findById(2)).thenReturn(Optional.empty());

        Space gottenSpace = spaceService.get(1);

        assertEquals(1, gottenSpace.getId());
        assertThrows(ElementNotFoundException.class, () -> spaceService.get(2));
        verify(spaceRepository).findById(1);
        verify(spaceRepository).findById(2);
    }

    @Test
    public void updateShouldReturnSpace() {
        when(spaceRepository.findById(1)).thenReturn(Optional.of(spaceWithId));
        when(spaceRepository.findById(2)).thenReturn(Optional.empty());

        Space updatedSpace = spaceService.update(1, spaceToUpdateWith);

        assertEquals(1, updatedSpace.getId());
        assertEquals(spaceToUpdateWith.getName(), updatedSpace.getName());
        assertEquals(spaceToUpdateWith.isShared(), updatedSpace.isShared());
        assertThrows(ElementNotFoundException.class, () -> spaceService.update(2, spaceToUpdateWith));
        verify(spaceRepository).findById(1);
        verify(spaceRepository).save(spaceWithId);
        verify(spaceRepository).findById(2);
    }

    @Test
    public void deleteShouldRemoveSpace() {
        when(spaceRepository.findById(1)).thenReturn(Optional.of(spaceWithId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> spaceService.delete(1));
        assertThrows(ElementNotFoundException.class, () -> spaceService.delete(1));
        verify(spaceRepository, times(2)).findById(1);
        verify(spaceRepository).delete(spaceWithId);
    }
}