package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.sbtitcourses.mdwiki.util.exception.ElementAlreadyExists;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Сервис с логикой CRUD операций над сущностью Space
 */
@Service
@Transactional(readOnly = true)
public class SpaceService implements SpaceCrudService {

    /**
     * Репозиторий для взаимодействия с сущностью Space
     */
    private final SpaceRepository spaceRepository;

    /**
     * Компонент для получения ресурсов
     */
    private final ResourceFetcher resourceFetcher;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param spaceRepository репозиторий для взаимодействия с сущностью Space
     * @param resourceFetcher компонент для получения ресурсов
     */
    @Autowired
    public SpaceService(SpaceRepository spaceRepository, ResourceFetcher resourceFetcher) {
        this.spaceRepository = spaceRepository;
        this.resourceFetcher = resourceFetcher;
    }

    /**
     * Метод, отвечающий за создание нового пространства
     * @param space пространство, которое нужно сохранить
     * @return сохраненное пространство
     */
    @Override
    @Transactional
    public Space create(Space space) {
        Person user = resourceFetcher.getLoggedInUser();

        if(spaceRepository.findByOwnerAndName(user, space.getName()).isPresent()){
            throw new ElementAlreadyExists("Пространство с таким именем уже существует");
        }

        space.setOwner(user);
        Date now = new Date();
        space.setCreatedAt(now);
        space.setUpdatedAt(now);
        space.setId(spaceRepository.save(space).getId());

        return space;
    }

    /**
     * Метод, отвечающий за получение всех публичных или пользовательских пространств
     * @param bunch номер страницы при пагинации
     * @param size количество элементов в странице при пагинации
     * @return список всех пространств
     */
    @Override
    public List<Space> get(int bunch, int size) {
        Person user = resourceFetcher.getLoggedInUser();

        Pageable pageable = PageRequest.of(bunch, size);

        return spaceRepository.findByOwnerOrSharedTrue(user, pageable);
    }

    /**
     * Метод, отвечающий за получение пространства
     * @param id ID пространства
     * @return найденое пространтво
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    public Space get(int id) throws AccessDeniedException {
        Space space = resourceFetcher.fetchSpace(id);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadSpaceDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        return space;
    }

    /**
     * Метод, отвечающий за обновление пространства
     * @param id ID пространства
     * @param spaceToUpdateWith пространство, значениями полей которого нужно обновить требуемое пространство
     * @return обновленное пространство
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Space update(int id, Space spaceToUpdateWith) throws AccessDeniedException {
        Space space = resourceFetcher.fetchSpace(id);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToUpdateSpaceDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        if(spaceRepository.findByOwnerAndName(user, spaceToUpdateWith.getName()).isPresent()){
            throw new ElementAlreadyExists("Пространство с таким именем уже существует");
        }

        space.setName(spaceToUpdateWith.getName());
        space.setShared(spaceToUpdateWith.isShared());

        spaceRepository.save(space);

        return space;
    }

    /**
     * Метод, отвечающий за удаление пространства
     * @param id ID пространства
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public void delete(int id) throws AccessDeniedException {
        Space space = resourceFetcher.fetchSpace(id);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToDeleteSpaceDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        spaceRepository.delete(space);
    }
}