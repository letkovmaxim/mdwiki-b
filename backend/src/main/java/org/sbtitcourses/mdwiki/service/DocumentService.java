package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.DocumentRepository;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис с логикой CRUD операций над сущностью Document
 */
@Service
@Transactional(readOnly = true)
public class DocumentService implements DocumentCrudService {

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
     * Компонент для проверки доступа к ресурсам
     */
    private final ResourceAccessHelper resourceAccessHelper;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param spaceRepository репозиторий для взаимодействия с сущностью Space
     * @param pageRepository репозиторий для взаимодействия с сущностью Page
     * @param documentRepository репозиторий для взаимодействия с сущностью Document
     * @param resourceAccessHelper компонент для проверки доступа к ресурсам
     */
    @Autowired
    public DocumentService(SpaceRepository spaceRepository,
                           PageRepository pageRepository,
                           DocumentRepository documentRepository,
                           ResourceAccessHelper resourceAccessHelper) {
        this.spaceRepository = spaceRepository;
        this.pageRepository = pageRepository;
        this.documentRepository = documentRepository;
        this.resourceAccessHelper = resourceAccessHelper;
    }

    /**
     * Метод, отвечающий за создание нового документа
     * @param document документ, который нужно сохранить
     * @param pageId ID страницы, в которой нужно создать документ
     * @param spaceId ID пространства, в котором нужно создать документ
     * @return сохраненный документ
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Document create(Document document, int pageId, int spaceId)
            throws ElementNotFoundException, AccessDeniedException{
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        Page page = pageRepository.findByIdAndSpace(pageId, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));

        if (resourceAccessHelper.isAccessToCreateDocumentDenied(page)) {
            throw new AccessDeniedException("Отказано в доступе");
        }

        document.setPage(page);
        document.setId(documentRepository.save(document).getId());

        return document;
    }

    /**
     * Метод, отвечающий за получение документа
     * @param pageId ID страницы, в которой нужно найти документ
     * @param spaceId ID пространства, в котором нужно найти документ
     * @return найденый документ
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Document get(int pageId, int spaceId) throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        Page page = pageRepository.findByIdAndSpace(pageId, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));

        return documentRepository.findByPage(page)
                .orElseThrow(() -> new ElementNotFoundException("Документ не найден"));
    }

    /**
     * Метод, отвечающий за обновление документа
     * @param pageId ID страницы, в которой нужно обновить документ
     * @param spaceId ID пространства, в котором нужно обновить документ
     * @param documentToUpdateWith документ, значениями полей которого нужно обновить требуемый документ
     * @return обновленный документ
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Document update(int pageId, int spaceId, Document documentToUpdateWith)
            throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        Page page = pageRepository.findByIdAndSpace(pageId, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));

        Document document = documentRepository.findByPage(page)
                .orElseThrow(() -> new ElementNotFoundException("Документ не найден"));

        document.setText(documentToUpdateWith.getText());

        documentRepository.save(document);

        return document;
    }

    /**
     * Метод, отвечающий за удаление документа
     * @param pageId ID страницы, в которой нужно удалить документ
     * @param spaceId ID пространства, в котором нужно удалить документ
     * @throws ElementNotFoundException если пространства с таким ID не существует
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public void delete(int pageId, int spaceId)
            throws ElementNotFoundException, AccessDeniedException {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));

        Page page = pageRepository.findByIdAndSpace(pageId, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));

        Document document = documentRepository.findByPage(page)
                .orElseThrow(() -> new ElementNotFoundException("Документ не найден"));

        page.setDocument(null);

        documentRepository.delete(document);
    }
}