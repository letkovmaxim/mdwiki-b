package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.SpaceRequest;
import org.sbtitcourses.mdwiki.dto.SpaceResponse;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.sbtitcourses.mdwiki.util.ErrorResponse;
import org.sbtitcourses.mdwiki.util.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/space")
public class SpaceController {

    private final SpaceService spaceService;
    private final ModelMapper modelMapper;

    @Autowired
    public SpaceController(SpaceService spaceService, ModelMapper modelMapper) {
        this.spaceService = spaceService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public SpaceResponse create(@RequestBody SpaceRequest spaceRequest) {
        Space spaceToCreate = modelMapper.map(spaceRequest, Space.class);

        return modelMapper.map(spaceService.create(spaceToCreate), SpaceResponse.class);
    }

    @GetMapping
    public List<SpaceResponse> getAll() {
        List<SpaceResponse> spaces = new LinkedList<>();
        for (Space space: spaceService.getAll()) {
            spaces.add(modelMapper.map(space, SpaceResponse.class));
        }

        return spaces;
    }

    @GetMapping("/{id}")
    public SpaceResponse get(@PathVariable int id) {
        Space space = spaceService.get(id);

        return modelMapper.map(space, SpaceResponse.class);
    }

    @PutMapping("/{id}")
    public SpaceResponse update(@PathVariable int id, @RequestBody SpaceRequest spaceRequest) {
        Space spaceToUpdate = modelMapper.map(spaceRequest, Space.class);

        return modelMapper.map(spaceService.update(id, spaceToUpdate), SpaceResponse.class);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        spaceService.delete(id);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Пространство не найдено",
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
