package org.sbtitcourses.mdwiki.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.model.StoredFile;
import org.sbtitcourses.mdwiki.repository.StoredFileRepository;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Тесты для {@link ImageStorageService}
 */
@ExtendWith(MockitoExtension.class)
class ImageStorageServiceTests {

    @Mock
    private StoredFileRepository storedFileRepository;
    @Mock
    private ResourceFetcher resourceFetcher;
    @InjectMocks
    private ImageStorageService imageStorageService;

    @Test
    public void createImageShouldReturnStoredFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[] {1}
        );
        StoredFile storedFile = new StoredFile();
        Person user = new Person(1);
        user.setUsername("user");
        Space space = new Space(1);
        space.setOwner(user);

        when(resourceFetcher.fetchSpace(1)).thenReturn(space);
        when(resourceFetcher.getLoggedInUser()).thenReturn(user);





    }
}