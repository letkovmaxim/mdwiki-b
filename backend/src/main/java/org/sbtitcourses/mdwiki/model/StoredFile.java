package org.sbtitcourses.mdwiki.model;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "file")
public class StoredFile {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "fileSequence")
    @SequenceGenerator(name = "fileSequence", initialValue = 1, allocationSize = 1, sequenceName = "file_sequence")
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "guid")
    private String GUID;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @ManyToMany
    @JoinTable(
            name = "file_page",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "page_id")
    )
    private List<Page> pages;

    public StoredFile() {
    }

    public StoredFile(String name, String GUID, Person owner, List<Page> pages) {
        this.name = name;
        this.GUID = GUID;
        this.owner = owner;
        this.pages = pages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGUID() {
        return GUID;
    }

    public void setGUID(String GUID) {
        this.GUID = GUID;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
}
