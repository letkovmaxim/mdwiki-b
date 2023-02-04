package org.sbtitcourses.mdwiki.controller;

import org.junit.jupiter.api.Test;
import org.sbtitcourses.mdwiki.model.StoredFile;
import org.sbtitcourses.mdwiki.service.ImageStorageService;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для {@link FileController}
 */
@WebMvcTest(FileController.class)
@AutoConfigureMockMvc(addFilters = false)
class FileControllerTests {

    @MockBean
    private ImageStorageService imageStorageService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void verifyHttpRequestMappingAndDeserialization() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1}
        );
        StoredFile storedFile = new StoredFile();

        when(imageStorageService.storeImage(file, 1, 32, 32)).thenReturn(storedFile);

        mockMvc.perform(multipart("/spaces/1/upload/image")
                        .file(file)
                        .param("thumbnailHeight", "32")
                        .param("thumbnailWidth", "32"))
                .andExpect(status().isOk());

        verify(imageStorageService).storeImage(file, 1, 32, 32);
    }

    @Test
    void verifyFieldValidation() throws Exception {
        mockMvc.perform(get("/user/uploads")
                        .param("bunch", "0")
                        .param("size", "0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void verifyErrorHandling() throws Exception {
        when(imageStorageService.loadImage("1e9d7290-71c7-4e20-bd88-5cd69f2c4746"))
                .thenThrow(ElementNotFoundException.class);
        when(imageStorageService.loadImage("72a88024-1ac2-4893-8409-eaf697c01a14"))
                .thenThrow(AccessDeniedException.class);

        mockMvc.perform(get("/download/image/{GUID}", "1e9d7290-71c7-4e20-bd88-5cd69f2c4746"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/download/image/{GUID}", "72a88024-1ac2-4893-8409-eaf697c01a14"))
                .andExpect(status().isForbidden());

        verify(imageStorageService).loadImage("1e9d7290-71c7-4e20-bd88-5cd69f2c4746");
        verify(imageStorageService).loadImage("72a88024-1ac2-4893-8409-eaf697c01a14");
    }
}