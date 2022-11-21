package org.sbtitcourses.mdwiki.controller;

import org.sbtitcourses.mdwiki.dto.file.FileUploadResponse;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.StoredFile;
import org.sbtitcourses.mdwiki.service.FileStorageService;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/spaces/{spaceId}/pages/{pageId}/uploadFile")
    public ResponseEntity<FileUploadResponse> uploadFileOnPage(@PathVariable("spaceId") int spaceId,
                                                               @PathVariable("pageId") int pageId,
                                                               @RequestParam("file") MultipartFile file) {
        StoredFile storedFile = fileStorageService.storeFileOnPage(file, pageId, spaceId);

        String downloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(String.format("/spaces/%d/pages/%d/downloadFile/", spaceId, pageId))
                .path(storedFile.getGUID())
                .toUriString();

        FileUploadResponse response = new FileUploadResponse(storedFile.getName(), downloadUri,
                file.getContentType(), file.getSize());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/spaces/{spaceId}/pages/{pageId}/downloadFile/{GUID}")
    public ResponseEntity<Resource> downloadFileFromPage(@PathVariable("spaceId") int spaceId,
                                                         @PathVariable("pageId") int pageId,
                                                         @PathVariable("GUID") String GUID,
                                                         HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResourceFromPage(GUID, pageId, spaceId);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
