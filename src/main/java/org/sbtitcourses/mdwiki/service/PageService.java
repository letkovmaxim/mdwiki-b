package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.util.EntityFetcher;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementAlreadyExistsException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Сервис с логикой взаимодействия с сущностью {@link Page}.
 */
@Service
@Transactional(readOnly = true)
public class PageService implements IPageService {

    /**
     * Репозиторий для взаимодействия с сущностью {@link Page}.
     */
    private final PageRepository pageRepository;

    /**
     * Компонент для получения сущностей.
     */
    private final EntityFetcher entityFetcher;

    /**
     * Конструктор для автоматичекого внедрения зависимостей.
     *
     * @param pageRepository репозиторий для взаимодействия с сущностью {@link Page}.
     * @param entityFetcher  компонент для получения ресурсов.
     */
    @Autowired
    public PageService(PageRepository pageRepository, EntityFetcher entityFetcher) {
        this.pageRepository = pageRepository;
        this.entityFetcher = entityFetcher;
    }

    /**
     * Метод, отвечающий за создание новой страницы.
     *
     * @param page    страница, которую нужно сохранить.
     * @param spaceId ID пространства, в котором нужно создать страницу.
     * @return сохраненную страницу.
     * @throws AccessDeniedException         если не удалось определить пользователя.
     * @throws ElementAlreadyExistsException если страница уже существует.
     */
    @Override
    @Transactional
    public Page create(Page page, int spaceId) {
        Space space = entityFetcher.fetchSpace(spaceId);
        Person user = entityFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToCreatePageDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        if (pageRepository.findBySpaceAndName(space, page.getName()).isPresent()) {
            throw new ElementAlreadyExistsException("Страница с таким именем уже существует в этом пространстве");
        }

        page.setSpace(space);
        Instant now = Instant.now();
        page.setCreatedAt(now);
        page.setUpdatedAt(now);
        page.setSubpages(Collections.emptyList());
        page.setId(pageRepository.save(page).getId());

        return page;
    }

    /**
     * Метод, отвечающий за создание подстраницы.
     *
     * @param subpage  подстраница, которую нужно сохранить.
     * @param parentId ID страницы-родителя, для которого нужно создать подстраницу.
     * @param spaceId  ID пространства, в котором нужно создать подстраницу.
     * @return сохраненную подстраницу.
     * @throws AccessDeniedException         если не удалось определить пользователя.
     * @throws ElementAlreadyExistsException если страница уже существует.
     */
    @Override
    @Transactional
    public Page createSubpage(Page subpage, int parentId, int spaceId) {
        Page parent = entityFetcher.fetchPage(parentId, spaceId);
        Person user = entityFetcher.getLoggedInUser();

        Space space = parent.getSpace();

        if (ResourceAccessHelper.isAccessToCreateSubpageDenied(parent, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        if (pageRepository.findBySpaceAndName(space, subpage.getName()).isPresent()) {
            throw new ElementAlreadyExistsException("Страница с таким именем уже существует в этом пространстве");
        }

        subpage.setSpace(parent.getSpace());
        subpage.setParent(parent);
        Instant now = Instant.now();
        subpage.setCreatedAt(now);
        subpage.setUpdatedAt(now);
        subpage.setId(pageRepository.save(subpage).getId());

        return subpage;
    }

    /**
     * Метод, отвечающий за получение всех страниц пространства.
     *
     * @param spaceId ID пространтсва, в котором нужно получить страницы.
     * @param bunch   номер страницы при пагинации.
     * @param size    количество элементов в странице при пагинации.
     * @return список всех страниц данного пространства.
     * @throws AccessDeniedException если не удалось определить пользователя.
     */
    @Override
    public List<Page> get(int spaceId, int bunch, int size) {
        Space space = entityFetcher.fetchSpace(spaceId);
        Person user = entityFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadAllPagesDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        Pageable pageable = PageRequest.of(bunch, size);

        return pageRepository.findBySpaceAndParentIsNullOrderById(space, pageable);
    }

    /**
     * Метод, отвечающий за получение записей в имени которых присутсвует pageSearch.
     *
     * @param pageSearch подстрока, для поиска записи.
     * @return список всех найденных записей.
     */
    @Override
    public List<Page> get(String pageSearch) {
        Person user = entityFetcher.getLoggedInUser();

        return pageRepository.findByOwnerAndNameStartingWith(user, pageSearch);
    }

    /**
     * Метод, отвечающий за получение страницы.
     *
     * @param pageId  ID страницы.
     * @param spaceId ID пространтсва, в котором нужно получить страницу.
     * @return найденую страницу.
     * @throws AccessDeniedException если не удалось определить пользователя.
     */
    @Override
    public Page get(int pageId, int spaceId) {
        Page page = entityFetcher.fetchPage(pageId, spaceId);
        Person user = entityFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadPageDenied(page, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        return page;
    }

    /**
     * Метод, отвечающий за получение страницы-родителя.
     *
     * @param pageId  ID страница, родителя которой нужно получить.
     * @param spaceId ID пространтсва, в котором нужно получить страницу.
     * @return найденую страницу.
     * @throws ElementNotFoundException если страница не найдена.
     * @throws AccessDeniedException    если не удалось определить пользователя.
     */
    @Override
    public Page getParent(int pageId, int spaceId) {
        Page page = entityFetcher.fetchPage(pageId, spaceId);
        Person user = entityFetcher.getLoggedInUser();

        Page parent = page.getParent();
        if (parent == null) {
            throw new ElementNotFoundException("Страница не найдена");
        }

        if (ResourceAccessHelper.isAccessToReadPageDenied(parent, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        return parent;
    }

    /**
     * Метод, отвечающий за обновление страницы.
     *
     * @param pageId           ID страницы.
     * @param spaceId          ID пространтсва, в котором нужно обновить страницу.
     * @param pageToUpdateWith информация о странице, которую нужно обновить.
     * @return обновленную страницу.
     * @throws AccessDeniedException         если не удалось определить пользователя.
     * @throws ElementAlreadyExistsException если страница уже существует.
     */
    @Override
    @Transactional
    public Page update(int pageId, int spaceId, Page pageToUpdateWith) {
        Page page = entityFetcher.fetchPage(pageId, spaceId);
        Person user = entityFetcher.getLoggedInUser();

        Space space = page.getSpace();

        if (ResourceAccessHelper.isAccessToUpdatePageDenied(page, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        if (!page.getName().equals(pageToUpdateWith.getName())) {
            if (pageRepository.findBySpaceAndName(space, pageToUpdateWith.getName()).isPresent()) {
                throw new ElementAlreadyExistsException("Страница с таким именем уже существует в этом пространстве");
            }
        }

        page.setName(pageToUpdateWith.getName());
        page.setShared(pageToUpdateWith.isShared());

        pageRepository.save(page);

        return page;
    }

    /**
     * Метод, отвечающий за удаление страницы.
     *
     * @param pageId  ID страницы.
     * @param spaceId ID пространтсва, в котором нужно удалить страницу.
     * @throws AccessDeniedException если не удалось определить пользователя.
     */
    @Override
    @Transactional
    public void delete(int pageId, int spaceId) {
        Page page = entityFetcher.fetchPage(pageId, spaceId);
        Person user = entityFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToDeletePageDenied(page, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        pageRepository.delete(page);
    }
}