package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Сервис с логикой CRUD операций над сущностью Page
 */
@Service
@Transactional(readOnly = true)
public class PageService implements PageCrudService {

    /**
     * Репозиторий для взаимодействия с сущностью Page
     */
    private final PageRepository pageRepository;

    /**
     * Компонент для получения ресурсов
     */
    private final ResourceFetcher resourceFetcher;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param pageRepository  репозиторий для взаимодействия с сущностью Page
     * @param resourceFetcher компонент для получения ресурсов
     */
    @Autowired
    public PageService(PageRepository pageRepository, ResourceFetcher resourceFetcher) {
        this.pageRepository = pageRepository;
        this.resourceFetcher = resourceFetcher;
    }

    /**
     * Метод, отвечающий за создание новой страницы
     * @param page страница, которую нужно сохранить
     * @param spaceId ID пространства, в котором нужно создать страницу
     * @return сохраненную страницу
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Page create(Page page, int spaceId) throws AccessDeniedException {
        Space space = resourceFetcher.fetchSpace(spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToCreatePageDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        page.setSpace(space);
        Date now = new Date();
        page.setCreatedAt(now);
        page.setUpdatedAt(now);
        page.setSubpages(Collections.emptyList());
        page.setId(pageRepository.save(page).getId());

        return page;
    }

    /**
     * Метод, отвечающий за создание подстраницы
     * @param subpage подстраница, которую нужно сохранить
     * @param parentId ID страницы-родителя, для которого нужно создать подстраницу
     * @param spaceId ID пространства, в котором нужно создать подстраницу
     * @return сохраненную подстраницу
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Page createSubpage(Page subpage, int parentId, int spaceId) throws AccessDeniedException {
        Page parent = resourceFetcher.fetchPage(parentId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToCreateSubpageDenied(parent, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        subpage.setSpace(parent.getSpace());
        subpage.setParent(parent);
        Date now = new Date();
        subpage.setCreatedAt(now);
        subpage.setUpdatedAt(now);
        subpage.setId(pageRepository.save(subpage).getId());

        return subpage;
    }

    /**
     * Метод, отвечающий за получение всех страниц пространства
     * @param spaceId ID пространтсва, в котором нужно получить страницы
     * @param bunch номер страницы при пагинации
     * @param size количество элементов в странице при пагинации
     * @return список всех страниц данного пространства
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    public List<Page> get(int spaceId, int bunch, int size) throws AccessDeniedException {
        Space space = resourceFetcher.fetchSpace(spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadAllPagesDenied(space, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        Pageable pageable = PageRequest.of(bunch, size);

        return pageRepository.findBySpaceAndParentIsNull(space, pageable);
    }

    /**
     * Метод, отвечающий за получение страницы
     * @param pageId ID страницы
     * @param spaceId ID пространтсва, в котором нужно получить страницу
     * @return найденую страницу
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    public Page get(int pageId, int spaceId) throws AccessDeniedException {
        Page page = resourceFetcher.fetchPage(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadPageDenied(page, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        return page;
    }

    /**
     * Метод, отвечающий за обновление страницы
     * @param pageId ID страницы
     * @param spaceId ID пространтсва, в котором нужно обновить страницу
     * @param pageToUpdateWith страница, значениями полей которой нужно обновить требуемую страницу
     * @return обновленную страницу
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Transactional
    public Page update(int pageId, int spaceId, Page pageToUpdateWith) throws AccessDeniedException {
        Page page = resourceFetcher.fetchPage(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToUpdatePageDenied(page, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        page.setName(pageToUpdateWith.getName());
        page.setShared(pageToUpdateWith.isShared());

        pageRepository.save(page);

        return page;
    }

    /**
     * Метод, отвечающий за удаление страницы
     * @param pageId ID страницы
     * @param spaceId ID пространтсва, в котором нужно удалить страницу
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Transactional
    public void delete(int pageId, int spaceId) throws AccessDeniedException {
        Page page = resourceFetcher.fetchPage(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToDeletePageDenied(page, user)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        pageRepository.delete(page);
    }
}