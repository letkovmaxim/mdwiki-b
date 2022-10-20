package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.space.SpaceRequest;
import org.sbtitcourses.mdwiki.dto.space.SpaceResponse;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.sbtitcourses.mdwiki.util.ErrorResponse;
import org.sbtitcourses.mdwiki.util.exception.NotFoundException;
import org.sbtitcourses.mdwiki.util.exception.SpaceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * REST контроллер для CRUD операций над сущностью Space
 */
@RestController
@RequestMapping("/spaces")
public class SpaceController {

    /**
     * Сервис с логикой CRUD операций над сущностью Space
     */
    private final SpaceService spaceService;
    /**
     * Маппер для конвертации сущностей
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param spaceService сервис с логикой CRUD операций над сущностью Space
     * @param modelMapper маппер для конвертации сущностей
     */
    @Autowired
    public SpaceController(SpaceService spaceService, ModelMapper modelMapper) {
        this.spaceService = spaceService;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, отвечающий за создание нового пространства
     * @param spaceRequest DTO сущности Space для запроса
     * @return DTO сущности Space для ответа с кодом 201
     */
    @PostMapping
    public ResponseEntity<SpaceResponse> create(@RequestBody SpaceRequest spaceRequest) {
        Space spaceToCreate = modelMapper.map(spaceRequest, Space.class);

        Space createdSpace = spaceService.create(spaceToCreate);

        SpaceResponse response = modelMapper.map(createdSpace, SpaceResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, отвечающий за получение всех пространств
     * @return список DTO сущности Space для ответа с кодом 200
     */
    @GetMapping
    public ResponseEntity<List<SpaceResponse>> getAll() {
        List<SpaceResponse> spaces = new LinkedList<>();

        for (Space space: spaceService.getAll()) {
            SpaceResponse spaceResponse = modelMapper.map(space, SpaceResponse.class);
            spaces.add(spaceResponse);
        }

        return new ResponseEntity<>(spaces, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за получение пространства по его ID
     * @param id ID пространства
     * @return DTO сущности Space для ответа с кодом 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpaceResponse> get(@PathVariable(name = "id") int id) {
        Space space = spaceService.get(id);

        SpaceResponse response = modelMapper.map(space, SpaceResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Метод, отвечающий за обновление пространства с заданым ID
     * @param id ID пространства
     * @param spaceRequest DTO сущности Space для запроса
     * @return DTO сущности Space для ответа с кодом 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpaceResponse> update(@PathVariable(name = "id") int id,
                                     @RequestBody SpaceRequest spaceRequest) {
        Space spaceToUpdateWith = modelMapper.map(spaceRequest, Space.class);

        Space updatedSpace = spaceService.update(id, spaceToUpdateWith);

        SpaceResponse response = modelMapper.map(updatedSpace, SpaceResponse.class);
        return new ResponseEntity<>(response, HttpStatus.OK) ;
    }

    /**
     * Метод, отвечающий за удаление пространтсва с заданым ID
     * @param id ID пространства
     * @return пустой ответ с кодом 205
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable int id) {
        spaceService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //TODO: VALIDATION, TESTS
}
