package org.sbtitcourses.mdwiki.dto.file;

/**
 * DTO с информацией о загруженном файле для ответа.
 */
public class FileUploadResponse {

    /**
     * Оригинальное название файла.
     */
    private String fileName;

    /**
     * Уникальный идентификатор файла.
     */
    private String fileGUID;

    /**
     * MIME-тип файла.
     */
    private String fileType;

    /**
     * Размер файла в битах.
     */
    private long size;

    /**
     * Конструктор для создания объекта исключения.
     *
     * @param fileName оригинальное название файла.
     * @param fileGUID уникальный идентификатор файла.
     * @param fileType MIME-тип файла.
     * @param size     размер файла в битах.
     */
    public FileUploadResponse(String fileName, String fileGUID, String fileType, long size) {
        this.fileName = fileName;
        this.fileGUID = fileGUID;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileGUID() {
        return fileGUID;
    }

    public void setFileGUID(String fileGUID) {
        this.fileGUID = fileGUID;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
