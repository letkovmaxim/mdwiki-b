package org.sbtitcourses.mdwiki.controller;

import org.modelmapper.ModelMapper;
import org.sbtitcourses.mdwiki.dto.space.SpaceRequest;
import org.sbtitcourses.mdwiki.dto.space.SpaceResponse;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.LinkedList;
import java.util.List;

/**
 * REST контроллер, обрабатывающий запросы на взаимодействие сущностью {@link Space}.
 */
@RestController
@RequestMapping("/spaces")
@Validated
public class SpaceController {

    /**
     * Сервис с логикой взаимодействия с сущностью {@link Space}.
     */
    private final SpaceService spaceService;

    /**
     * Маппер для конвертации сущностей.
     */
    private final ModelMapper modelMapper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей.
     *
     * @param spaceService сервис с логикой взаимодействия с сущностью {@link Space}.
     * @param modelMapper  маппер для конвертации сущностей.
     */
    @Autowired
    public SpaceController(SpaceService spaceService, ModelMapper modelMapper) {
        this.spaceService = spaceService;
        this.modelMapper = modelMapper;
    }

    /**
     * Метод, обрабатывающий запрос на создание нового пространства.
     *
     * @param spaceRequest информация о новом пространстве.
     * @return HTTP ответ с информацией о новом пространстве и статусом 201.
     */
    @PostMapping
    public ResponseEntity<SpaceResponse> create(@RequestBody @Valid SpaceRequest spaceRequest) {
        Space space = modelMapper.map(spaceRequest, Space.class);

        Space createdSpace = spaceService.create(space);

        SpaceResponse response = modelMapper.map(createdSpace, SpaceResponse.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Метод, обрабатывающий запрос на получение всех пространств пользователя.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов на странице при пагинации.
     * @return HTTP ответ со списком пространств и статусом 200.
     */
    @GetMapping
    public ResponseEntity<List<SpaceResponse>> get(@RequestParam("bunch") @Min(0) int bunch,
                                                   @RequestParam("size") @Min(1) int size) {
        List<SpaceResponse> spaces = new LinkedList<>();

        for (Space space : spaceService.get(bunch, size)) {
            SpaceResponse spaceResponse = modelMapper.map(space, SpaceResponse.class);
            spaces.add(spaceResponse);
        }

        return ResponseEntity.ok().body(spaces);
    }

    /**
     * Метод, обрабатывающий запрос на получение всех публичных пространств.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов на странице при пагинации.
     * @return HTTP ответ со списком пространств и статусом 200.
     */
    @GetMapping("/shared")
    public ResponseEntity<List<SpaceResponse>> getShared(@RequestParam("bunch") @Min(0) int bunch,
                                                         @RequestParam("size") @Min(1) int size) {
        List<SpaceResponse> spaces = new LinkedList<>();

        for (Space space : spaceService.getShared(bunch, size)) {
            SpaceResponse spaceResponse = modelMapper.map(space, SpaceResponse.class);
            spaces.add(spaceResponse);
        }

        return ResponseEntity.ok().body(spaces);
    }

    /**
     * Метод, обрабатывающий запрос на получение пространства по его ID.
     *
     * @param id ID пространства.
     * @return HTTP ответ с информацией о пространстве и статусом 200.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SpaceResponse> get(@PathVariable("id") int id) {
        Space space = spaceService.get(id);

        SpaceResponse response = modelMapper.map(space, SpaceResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на обновление пространства по его ID.
     *
     * @param id           ID пространства.
     * @param spaceRequest информация о пространстве, которую нужно обновить.
     * @return HTTP ответ с информацией об обновленном пространстве и статусом 200.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SpaceResponse> update(@PathVariable("id") int id,
                                                @RequestBody @Valid SpaceRequest spaceRequest) {
        Space spaceToUpdateWith = modelMapper.map(spaceRequest, Space.class);

        Space updatedSpace = spaceService.update(id, spaceToUpdateWith);

        SpaceResponse response = modelMapper.map(updatedSpace, SpaceResponse.class);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Метод, обрабатывающий запрос на удаление пространтсва по его ID.
     *
     * @param id ID пространства.
     * @return HTTP ответ со статусом 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        spaceService.delete(id);

        return ResponseEntity.noContent().build();
    }
}