package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.util.exception.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PageService {

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
    public List<Page> getAll(Space space) {
        return pageRepository.findBySpaceAndParentIsNull(space);
    }

    /**
     * Метод, отвечающий за получение страницы данного пространтсва по его ID
     * @param id ID страницы
     * @param space пространтсво, для которого нужно получить страницу
     * @return найденую страницу
     * @throws PageNotFoundException если страницы с таким ID не существует
     */
    public Page get(int id, Space space) throws PageNotFoundException {
        return pageRepository.findByIdAndSpace(id, space).orElseThrow(PageNotFoundException::new);
    }

    /**
     * Метод, отвечающий за обновление страницы данного пространства по его ID
     * @param id ID страницы
     * @param space пространтсво, для которого нужно обновить страницу
     * @param pageToUpdateWith страница, значениями полей которой нужно обновить требуемую страницу
     * @return обновленную страницу
     * @throws PageNotFoundException если страницы с таким ID не существует
     */
    @Transactional
    public Page update(int id, Space space,  Page pageToUpdateWith) throws PageNotFoundException {
        Page pageToUpdate = pageRepository.findByIdAndSpace(id, space).orElseThrow(PageNotFoundException::new);
        pageToUpdate.setName(pageToUpdateWith.getName());
        pageToUpdate.setPublic(pageToUpdateWith.getPublic());

        pageRepository.save(pageToUpdate);

        return pageToUpdate;
    }

    /**
     * Метод, отвечающий за удаление страницы данного пространства по его ID
     * @param id ID
     * @throws PageNotFoundException если страницы с таким ID не существует
     */
    @Transactional
    public void delete(int id) throws PageNotFoundException {
        Page pageToDelete = pageRepository.findById(id).orElseThrow(PageNotFoundException::new);
        pageRepository.delete(pageToDelete);
    }
}