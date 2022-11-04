package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
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
     * Репозиторий для взаимодействия с сущностью Space
     */
    private final SpaceRepository spaceRepository;

    /**
     * Репозиторий для взаимодействия с сущностью Page
     */
    private final PageRepository pageRepository;

    /**
     * Компонент для проверки доступа к ресурсам
     */
    private final ResourceAccessHelper resourceAccessHelper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param spaceRepository репозиторий для взаимодействия с сущностью Space
     * @param pageRepository репозиторий для взаимодействия с сущностью Page
     * @param resourceAccessHelper компонент для проверки доступа к ресурсам
     */
    @Autowired
    public PageService(SpaceRepository spaceRepository,
                       PageRepository pageRepository,
                       ResourceAccessHelper resourceAccessHelper) {
        this.spaceRepository = spaceRepository;
        this.pageRepository = pageRepository;
        this.resourceAccessHelper = resourceAccessHelper;
    }

    /**
     * Метод, отвечающий за создание новой страницы
     * @param page страница, которую нужно сохранить
     * @param spaceId ID пространства, в котором нужно создать страницу
     * @return сохраненную страницу
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Page create(Page page, int spaceId) throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        if (resourceAccessHelper.isAccessToCreatePageDenied(space)) {
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
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Page createSubpage(Page subpage, int parentId, int spaceId)
            throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        Page parent = pageRepository.findByIdAndSpace(parentId, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));

        if (resourceAccessHelper.isAccessToCreateSubpageDenied(parent)) {
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
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    public List<Page> get(int spaceId, int bunch, int size) throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        if (resourceAccessHelper.isAccessToReadAllPagesDenied(space)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        Pageable pageable = PageRequest.of(bunch, size);

        return pageRepository.findBySpaceAndParentIsNull(space, pageable);
    }

    /**
     * Метод, отвечающий за получение страницы
     * @param id ID страницы
     * @param spaceId ID пространтсва, в котором нужно получить страницу
     * @return найденую страницу
     * @throws ElementNotFoundException если страницы с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    public Page get(int id, int spaceId) throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        Page page = pageRepository.findByIdAndSpace(id, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));

        if (resourceAccessHelper.isAccessToReadPageDenied(page)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        return page;
    }

    /**
     * Метод, отвечающий за обновление страницы
     * @param id ID страницы
     * @param spaceId ID пространтсва, в котором нужно обновить страницу
     * @param pageToUpdateWith страница, значениями полей которой нужно обновить требуемую страницу
     * @return обновленную страницу
     * @throws ElementNotFoundException если страницы с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Transactional
    public Page update(int id, int spaceId, Page pageToUpdateWith)
            throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        Page page = pageRepository.findByIdAndSpace(id, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));

        if (resourceAccessHelper.isAccessToUpdatePageDenied(page)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        page.setName(pageToUpdateWith.getName());
        page.setPublic(pageToUpdateWith.isPublic());

        pageRepository.save(page);

        return page;
    }

    /**
     * Метод, отвечающий за удаление страницы
     * @param id ID страницы
     * @param spaceId ID пространтсва, в котором нужно удалить страницу
     * @throws ElementNotFoundException если страницы с таким ID не существует
     */
    @Transactional
    public void delete(int id, int spaceId) throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        Page page = pageRepository.findByIdAndSpace(id, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));

        if (resourceAccessHelper.isAccessToDeletePageDenied(page)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        pageRepository.delete(page);
    }
}