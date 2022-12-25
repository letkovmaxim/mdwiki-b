package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.ConvertedDocument;
import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.repository.DocumentRepository;
import org.sbtitcourses.mdwiki.util.PdfConverter;
import org.sbtitcourses.mdwiki.util.ResourceAccessHelper;
import org.sbtitcourses.mdwiki.util.ResourceFetcher;
import org.sbtitcourses.mdwiki.util.exception.AccessDeniedException;
import org.sbtitcourses.mdwiki.util.exception.PdfConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
     * Компонент для получения ресурсов
     */
    private final ResourceFetcher resourceFetcher;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param documentRepository репозиторий для взаимодействия с сущностью Document
     * @param resourceFetcher компонент для получения ресурсов
     */
    @Autowired
    public DocumentService(DocumentRepository documentRepository,
                           ResourceFetcher resourceFetcher) {
        this.documentRepository = documentRepository;
        this.resourceFetcher = resourceFetcher;
    }

    /**
     * Метод, отвечающий за создание нового документа
     * @param document документ, который нужно сохранить
     * @param pageId ID страницы, в которой нужно создать документ
     * @param spaceId ID пространства, в котором нужно создать документ
     * @return сохраненный документ
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Document create(Document document, int pageId, int spaceId) throws AccessDeniedException{
        Page page = resourceFetcher.fetchPage(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToCreateDocumentDenied(page, user)) {
            throw new AccessDeniedException("Доступ запрещен");
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
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Document get(int pageId, int spaceId) throws AccessDeniedException {
        Document document = resourceFetcher.fetchDocument(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadDocumentDenied(document, user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        return document;
    }

    /**
     * Метод, отвечающий за обновление документа
     * @param pageId ID страницы, в которой нужно обновить документ
     * @param spaceId ID пространства, в котором нужно обновить документ
     * @param documentToUpdateWith документ, значениями полей которого нужно обновить требуемый документ
     * @return обновленный документ
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public Document update(int pageId, int spaceId, Document documentToUpdateWith) throws AccessDeniedException {
        Document document = resourceFetcher.fetchDocument(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToUpdateDocumentDenied(document, user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        document.setText(documentToUpdateWith.getText());

        documentRepository.save(document);

        return document;
    }

    /**
     * Метод, отвечающий за удаление документа
     * @param pageId ID страницы, в которой нужно удалить документ
     * @param spaceId ID пространства, в котором нужно удалить документ
     * @throws AccessDeniedException если не удалось определить пользователя
     */
    @Override
    @Transactional
    public void delete(int pageId, int spaceId) throws AccessDeniedException {
        Document document = resourceFetcher.fetchDocument(pageId, spaceId);
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToDeleteDocumentDenied(document, user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        document.getPage().setDocument(null);

        documentRepository.delete(document);
    }

    /**
     * Метод, отвечающий за конвертацию докумета в PDF формат
     * @param spaceId ID пространства
     * @param pageId ID страницы
     * @param font название шрифта
     * @param size размер шрифта в писелях
     * @return поток данных с документом в формате PDF
     * @throws AccessDeniedException если не удалось определить пользователя
     * @throws PdfConversionException если возникла ошибка конвертации
     */
    @Override
    public ConvertedDocument convertToPdf(int spaceId, int pageId, String font, int size, boolean tree) {
        Page page = resourceFetcher.fetchPage(pageId, spaceId);
        Document document = page.getDocument();
        Person user = resourceFetcher.getLoggedInUser();

        if (ResourceAccessHelper.isAccessToReadDocumentDenied(document, user)) {
            throw new AccessDeniedException("Доступ запрещен");
        }

        PdfConverter converter = PdfConverter
                .builder()
                .font(font)
                .size(size)
                .build();

        String markdown = tree ? treeOf(page) : document.getText();
        String documentName = document.getPage().getName();

        InputStreamResource inputStreamResource = converter
                .convert(markdown);

        return new ConvertedDocument(inputStreamResource, documentName);
    }

    private String treeOf(Page page) {
        StringBuilder markdown = new StringBuilder();
        readTree(page, markdown);
        return markdown.toString();
    }

    private void readTree(Page page, StringBuilder markdown) {
        markdown.append(page.getDocument().getText());
        for (Page subpage: page.getSubpages()) {
            markdown.append("\n");
            readTree(subpage, markdown);
        }
    }
}