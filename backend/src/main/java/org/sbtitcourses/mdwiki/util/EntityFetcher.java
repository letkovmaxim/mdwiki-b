package org.sbtitcourses.mdwiki.util;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.DocumentRepository;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.security.PersonDetails;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Компонент для получения ресурсов
 */
@Component
public class EntityFetcher {

    /**
     * Репозиторий для взаимодействия с сущностью Person
     */
    private final PersonRepository personRepository;

    /**
     * Репозиторий для взаимодействия с сущностью Space
     */
    private final SpaceRepository spaceRepository;

    /**
     * Репозиторий для взаимодействия с сущностью Page
     */
    private final PageRepository pageRepository;

    /**
     * Репозиторий для взаимодействия с сущностью Document
     */
    private final DocumentRepository documentRepository;

    /**
     * Конструктор для автоматического внедрения зависимостей
     * @param personRepository репозиторий для взаимодействия с сущностью Person
     * @param spaceRepository репозиторий для взаимодействия с сущностью Space
     * @param pageRepository репозиторий для взаимодействия с сущностью Page
     * @param documentRepository репозиторий для взаимодействия с сущностью Document
     */
    @Autowired
    public EntityFetcher(PersonRepository personRepository,
                         SpaceRepository spaceRepository,
                         PageRepository pageRepository,
                         DocumentRepository documentRepository) {
        this.personRepository = personRepository;
        this.spaceRepository = spaceRepository;
        this.pageRepository = pageRepository;
        this.documentRepository = documentRepository;
    }

    /**
     * Метод, отвечающий за получение пространства
     * @param spaceId ID пространства
     * @return найденное пространство
     * @throws ElementNotFoundException если пространство с таким ID не найдено
     */
    public Space fetchSpace(int spaceId) throws ElementNotFoundException {
        return spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));
    }

    /**
     * Метод, отвечающий за получение страницы
     * @param pageId ID страницы
     * @param spaceId ID пространства
     * @return найденную страницу
     * @throws ElementNotFoundException если пространство или страница не найдены
     */
    public Page fetchPage(int pageId, int spaceId) throws ElementNotFoundException {
        Space space = fetchSpace(spaceId);

        return pageRepository.findByIdAndSpace(pageId, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));
    }

    /**
     * Метод, отвечающий за получение документа
     * @param pageId ID страницы, для которой нужно получить документ
     * @param spaceId ID пространства
     * @return найденный документ
     * @throws ElementNotFoundException если пространство, страница или документ не найдены
     */
    public Document fetchDocument(int pageId, int spaceId) throws ElementNotFoundException {
        Page page = fetchPage(pageId, spaceId);

        return documentRepository.findByPage(page)
                .orElseThrow(() -> new ElementNotFoundException("Документ не найден"));
    }

    /**
     * Метод, отвечающий за получение объекта пользователя
     * @return Optional с объектом пользователя, если он прошел аутентифицию, пустой optional в противном случае
     * @throws ElementNotFoundException если пользователь не найден
     */
    public Person getLoggedInUser() throws ElementNotFoundException {
        PersonDetails principal = (PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        return personRepository.findByUsername(username)
                .orElseThrow(() -> new ElementNotFoundException("Пользователь не найден"));
    }
}
