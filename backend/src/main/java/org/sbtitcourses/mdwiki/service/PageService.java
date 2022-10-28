package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PageRepository;
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
     * Конструктор для автоматичекого внедрения зависимостей
     * @param pageRepository репозиторий для взаимодействия с сущностью Page
     */
    @Autowired
    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    /**
     * Метод, отвечающий за создание новой страницы
     * @param pageToCreate страница, которую нужно сохранить
     * @return сохраненную страницу
     */
    @Override
    @Transactional
    public Page create(Page pageToCreate) {
        Date now = new Date();
        pageToCreate.setCreatedAt(now);
        pageToCreate.setUpdatedAt(now);
        pageToCreate.setSubpages(Collections.emptyList());
        pageToCreate.setId(pageRepository.save(pageToCreate).getId());

        return pageToCreate;
    }

    /**
     * Метод, отвечающий за получение всех страниц данного пространства из репозитория
     * @param space пространтсво, для которого нужно получить страницы
     * @return список всех страниц данного пространства
     */
    @Override
    public List<Page> getAll(Space space, int bunch, int size) {
        Pageable pageable = PageRequest.of(bunch, size);
        return pageRepository.findBySpaceAndParentIsNull(space, pageable);
    }

    /**
     * Метод, отвечающий за получение страницы данного пространтсва по его ID
     * @param id ID страницы
     * @param space пространтсво, для которого нужно получить страницу
     * @return найденую страницу
     * @throws ElementNotFoundException если страницы с таким ID не существует
     */
    @Override
    public Page get(int id, Space space) throws ElementNotFoundException {
        return pageRepository.findByIdAndSpace(id, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));
    }

    /**
     * Метод, отвечающий за обновление страницы данного пространства по его ID
     * @param id ID страницы
     * @param space пространтсво, для которого нужно обновить страницу
     * @param pageToUpdateWith страница, значениями полей которой нужно обновить требуемую страницу
     * @return обновленную страницу
     * @throws ElementNotFoundException если страницы с таким ID не существует
     */
    @Override
    @Transactional
    public Page update(int id, Space space, Page pageToUpdateWith) throws ElementNotFoundException {
        Page pageToUpdate = pageRepository.findByIdAndSpace(id, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));
        pageToUpdate.setName(pageToUpdateWith.getName());
        pageToUpdate.setPublic(pageToUpdateWith.getPublic());

        pageRepository.save(pageToUpdate);

        return pageToUpdate;
    }

    /**
     * Метод, отвечающий за удаление страницы данного пространства по его ID
     * @param id ID
     * @throws ElementNotFoundException если страницы с таким ID не существует
     */
    @Override
    @Transactional
    public void delete(int id) throws ElementNotFoundException {
        Page pageToDelete = pageRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));
        pageRepository.delete(pageToDelete);
    }
}