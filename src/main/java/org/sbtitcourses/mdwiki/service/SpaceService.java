package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.EntityFetcher;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Сервис с логикой взаимодействия с сущностью {@link Space}.
 */
@Service
@Transactional(readOnly = true)
public class SpaceService implements ISpaceService {

    /**
     * Репозиторий для взаимодействия с сущностью {@link Space}.
     */
    private final SpaceRepository spaceRepository;

    /**
     * Компонент для получения сущностей.
     */
    private final EntityFetcher entityFetcher;

    /**
     * Конструктор для автоматичекого внедрения зависимостей.
     *
     * @param spaceRepository репозиторий для взаимодействия с сущностью {@link Space}.
     * @param entityFetcher   компонент для получения ресурсов.
     */
    @Autowired
    public SpaceService(SpaceRepository spaceRepository, EntityFetcher entityFetcher) {
        this.spaceRepository = spaceRepository;
        this.entityFetcher = entityFetcher;
    }

    /**
     * Метод, отвечающий за создание нового пространства.
     *
     * @param space пространство, которое нужно сохранить.
     * @return сохраненное пространство.
     * @throws ElementAlreadyExistsException если пространство уже существует.
     */
    @Override
    @Transactional
    public Space create(Space space) {
        Person user = entityFetcher.getLoggedInUser();

        if (spaceRepository.findByOwnerAndName(user, space.getName()).isPresent()) {
            throw new ElementAlreadyExistsException("Пространство с таким именем уже существует");
        }

        space.setOwner(user);
        Instant now = Instant.now();
        space.setCreatedAt(now);
        space.setUpdatedAt(now);
        space.setId(spaceRepository.save(space).getId());

        return space;
    }

    /**
     * Метод, отвечающий за получение всех пользовательских пространств.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов в странице при пагинации.
     * @return список пользовательских всех пространств.
     */
    @Override
    public List<Space> get(int bunch, int size) {
        Person user = entityFetcher.getLoggedInUser();

        Pageable pageable = PageRequest.of(bunch, size);

        return spaceRepository.findByOwnerOrderById(user, pageable);
    }

    /**
     * Метод, отвечающий за получение всех публичных пространств.
     *
     * @param bunch номер страницы при пагинации.
     * @param size  количество элементов в странице при пагинации.
     * @return список всех публичных пространств.
     */
    @Override
    public List<Space> getShared(int bunch, int size) {
        Pageable pageable = PageRequest.of(bunch, size);

        return spaceRepository.findBySharedTrue(pageable);
    }

    /**
     * Метод, отвечающий за получение пространства.
     *
     * @param id ID пространства.
     * @return найденое пространтво.
     * @throws AccessDeniedException если не удалось определить пользователя.
     */
    @Override
    public Space get(int id) {
        Space space = entityFetcher.fetchSpace(id);
        Person user = entityFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadSpaceDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        return space;
    }

    /**
     * Метод, отвечающий за обновление пространства.
     *
     * @param id                ID пространства.
     * @param spaceToUpdateWith информация о пространстве, которую нужно обновить.
     * @return обновленное пространство.
     * @throws AccessDeniedException         если не удалось определить пользователя.
     * @throws ElementAlreadyExistsException если пространство уже существует.
     */
    @Override
    @Transactional
    public Space update(int id, Space spaceToUpdateWith) {
        Space space = entityFetcher.fetchSpace(id);
        Person user = entityFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToUpdateSpaceDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        if (!space.getName().equals(spaceToUpdateWith.getName())) {
            if (spaceRepository.findByOwnerAndName(user, spaceToUpdateWith.getName()).isPresent()) {
                throw new ElementAlreadyExistsException("Пространство с таким именем уже существует");
            }
        }

        space.setName(spaceToUpdateWith.getName());
        space.setShared(spaceToUpdateWith.isShared());

        spaceRepository.save(space);

        return space;
    }

    /**
     * Метод, отвечающий за удаление пространства.
     *
     * @param id ID пространства.
     * @throws AccessDeniedException если не удалось определить пользователя.
     */
    @Override
    @Transactional
    public void delete(int id) {
        Space space = entityFetcher.fetchSpace(id);
        Person user = entityFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToDeleteSpaceDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        spaceRepository.delete(space);
    }
}