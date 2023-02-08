package org.sbtitcourses.mdwiki.util;

import org.springframework.core.io.InputStreamResource;

/**
 * Вспомогательный класс для хранения документа в виде ресурса и его названия.
 */
public class ConvertedDocument {

    /**
     * Объект, представляющий PDF файл в виде ресурса.
     */
    private final InputStreamResource inputStreamResource;

    /**
     * Название документа.
     */
    private final String documentName;

    /**
     * Конструктор для создания объекта класса.
     *
     * @param inputStreamResource объект, представляющий PDF файл в виде ресурса.
     * @param documentName        название документа.
     */
    public ConvertedDocument(InputStreamResource inputStreamResource, String documentName) {
        this.inputStreamResource = inputStreamResource;
        this.documentName = documentName;
    }

    public InputStreamResource getInputStreamResource() {
        return inputStreamResource;
    }

    public String getDocumentName() {
        return documentName;
    }
}