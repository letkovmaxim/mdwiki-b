package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
     * Компонент для проверки доступа к ресурсам
     */
    private final ResourceAccessHelper resourceAccessHelper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param spaceRepository репозиторий для взаимодействия с сущностью Space
     * @param resourceAccessHelper компонент для проверки доступа к ресурсам
     */
    @Autowired
    public SpaceService(SpaceRepository spaceRepository, ResourceAccessHelper resourceAccessHelper) {
        this.spaceRepository = spaceRepository;
        this.resourceAccessHelper = resourceAccessHelper;
    }

    /**
     * Метод, отвечающий за создание нового пространства
     * @param space пространство, которое нужно сохранить
     * @return сохраненное пространство
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Space create(Space space) throws AccessDeniedException {
        Person owner = resourceAccessHelper.getLoggedInUser()
                .orElseThrow(() -> new AccessDeniedException("Отказано в доступе"));

        space.setOwner(owner);
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
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    public List<Space> get(int bunch, int size) throws AccessDeniedException {
        Person owner = resourceAccessHelper.getLoggedInUser()
                .orElseThrow(() -> new AccessDeniedException("Отказано в доступе"));

        Pageable pageable = PageRequest.of(bunch, size);

        return spaceRepository.findByOwnerOrSharedTrue(owner, pageable);
    }

    /**
     * Метод, отвечающий за получение пространства
     * @param id ID пространства
     * @return найденое пространтво
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    public Space get(int id) throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        if (resourceAccessHelper.isAccessToReadSpaceDenied(space)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        return space;
    }

    /**
     * Метод, отвечающий за обновление пространства
     * @param id ID пространства
     * @param spaceToUpdateWith пространство, значениями полей которого нужно обновить требуемое пространство
     * @return обновленное пространство
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Space update(int id, Space spaceToUpdateWith) throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        if (resourceAccessHelper.isAccessToUpdateSpaceDenied(space)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        space.setName(spaceToUpdateWith.getName());
        space.setShared(spaceToUpdateWith.isShared());

        spaceRepository.save(space);

        return space;
    }

    /**
     * Метод, отвечающий за удаление пространства
     * @param id ID пространства
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public void delete(int id) throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        if (resourceAccessHelper.isAccessToDeleteSpaceDenied(space)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        spaceRepository.delete(space);
    }
}