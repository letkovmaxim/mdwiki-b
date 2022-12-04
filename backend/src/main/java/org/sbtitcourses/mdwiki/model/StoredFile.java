package org.sbtitcourses.mdwiki.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * Сущность записанных в системе файлов
 * для хранения информации о них в базе данных
 */
@Entity
@Table(name = "file")
public class StoredFile {

    /**
     * ID файла в базе данных
     */
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "fileSequence")
    @SequenceGenerator(name = "fileSequence", initialValue = 1, allocationSize = 1, sequenceName = "file_sequence")
    @Column(name = "id", nullable = false)
    private int id;

    /**
     * Уникальный идентификатор файла
     */
    @Column(name = "guid")
    private String GUID;

    /**
     * Оригинальное название файла
     */
    @Column(name = "name")
    private String originalName;

    /**
     * MIME-тип файла
     */
    @Column(name = "type")
    private String MimeType;

    /**
     * Размер файла в битах
     */
    @Column(name = "size")
    private long size;

    /**
     * Пользователь-владелец файла
     */
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    /**
     * Пространство, с которым связан файл
     */
    @ManyToOne
    @JoinColumn(name = "space_id", referencedColumnName = "id")
    private Space space;

    public StoredFile() {
    }

    public StoredFile(String GUID, String originalName, String MimeType,
                      long size, Person owner, Space space) {
        this.GUID = GUID;
        this.originalName = originalName;
        this.MimeType = MimeType;
        this.size = size;
        this.owner = owner;
        this.space = space;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String MimeType) {
        this.MimeType = MimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }
}
