package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.repository.DocumentRepository;
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
     * Репозиторий для взаимодействия с сущностью Document
     */
    private final DocumentRepository documentRepository;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param documentRepository репозиторий для взаимодействия с сущностью Document
     */
    @Autowired
    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    /**
     * Метод, отвечающий за создание нового документа
     * @param documentToCreate документ, который нужно сохранить
     * @return сохраненный документ
     */
    @Override
    @Transactional
    public Document create(Document documentToCreate) {
        documentToCreate.setId(documentRepository.save(documentToCreate).getId());

        return documentToCreate;
    }

    /**
     * Метод, отвечающий за получение документа данной страницы
     * @param page страница, для которой нужно получить документ
     * @return найденый документ
     * @throws ElementNotFoundException если для данной страница документа не существует
     */
    @Override
    @Transactional
    public Document get(Page page) throws ElementNotFoundException {
        return documentRepository.findByPage(page)
                .orElseThrow(() -> new ElementNotFoundException("Документ не найден"));
    }

    /**
     * Метод, отвечающий за обновление документа данной страницы
     * @param page страница, для которой нужно обновить документ
     * @param documentToUpdateWith документ, значениями полей которого нужно обновить требуемый документ
     * @return обновленный документ
     * @throws ElementNotFoundException если для данной страница документа не существует
     */
    @Override
    @Transactional
    public Document update(Page page, Document documentToUpdateWith) throws ElementNotFoundException {
        Document documentToUpdate = documentRepository.findByPage(page)
                .orElseThrow(() -> new ElementNotFoundException("Документ не найден"));
        documentToUpdate.setText(documentToUpdateWith.getText());

        documentRepository.save(documentToUpdate);

        return documentToUpdate;
    }

    /**
     * Метод, отвечающий за удаление документа данной страницы
     * @param page страница, для которой нужно удалить документ
     * @throws ElementNotFoundException если для данной страница документа не существует
     */
    @Override
    @Transactional
    public void delete(Page page) throws ElementNotFoundException {
        Document documentToDelete = documentRepository.findByPage(page)
                .orElseThrow(() -> new ElementNotFoundException("Документ не найден"));
        page.setDocument(null);
        documentRepository.delete(documentToDelete);
    }
}