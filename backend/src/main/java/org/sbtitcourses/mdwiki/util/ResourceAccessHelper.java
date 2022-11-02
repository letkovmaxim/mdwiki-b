package org.sbtitcourses.mdwiki.util;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PersonRepository;
import org.sbtitcourses.mdwiki.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResourceAccessHelper {

    private final PersonRepository personRepository;

    @Autowired
    public ResourceAccessHelper(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    //////////////SPACE//////////////////

    public boolean isAccessToCreateSpaceDenied() {
        Optional<Person> user = getLoggedInUser();

        return user.isEmpty();
    }

    public boolean isAccessToReadSpaceDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> space.isPublic() || person.getId() == space.getOwner().getId()).isEmpty();
    }

    public boolean isAccessToUpdateSpaceDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == space.getOwner().getId()).isEmpty();
    }

    public boolean isAccessToDeleteSpaceDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == space.getOwner().getId()).isEmpty();
    }

    //////////////////PAGE//////////////////////

    public boolean isAccessToCreatePageDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == space.getOwner().getId()).isEmpty();
        //TODO: this == access to update space (???)
    }

    public boolean isAccessToCreateSubpageDenied(Page parent) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == parent.getOwner().getId()).isEmpty();
    }

    public boolean isAccessToReadPageDenied(Page page) {
        Optional<Person> user = getLoggedInUser();

        if (user.isPresent()) {
            if (user.get().getId() == page.getOwner().getId() ||
                    page.getSpace().isPublic()) {
                return false;
            }

            return isAccessToReadRootPageDenied(page);
        }

        return true;
    }

    private boolean isAccessToReadRootPageDenied(Page page) {
        if (page.getParent() != null) {
            return isAccessToReadRootPageDenied(page.getParent());
        }

        return !page.isPublic();
    }

    public boolean isAccessToUpdatePageDenied(Page page) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == page.getOwner().getId()).isEmpty();
    }

    public boolean isAccessToDeletePageDenied(Page page) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == page.getOwner().getId()).isEmpty();
    }

    ////////////////DOCUMENT///////////////////

    public boolean isAccessToCreateDocumentDenied(Page page) {
        return isAccessToCreatePageDenied(page.getSpace());
    }

    public boolean isAccessToReadDocumentDenied(Document document) {
       return isAccessToReadPageDenied(document.getPage());
    }

    public boolean isAccessToUpdateDocumentDenied(Document document) {
        return isAccessToUpdatePageDenied(document.getPage());
    }

    public boolean isAccessToDeleteDocumentDenied(Document document) {
        return isAccessToDeletePageDenied(document.getPage());
    }

    /////////////////////////////////////////////

    public Optional<Person> getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof PersonDetails) {
            String username = ((PersonDetails) principal).getUsername();

            return personRepository.findByUsername(username);
        }

        return Optional.empty();
    }
}
