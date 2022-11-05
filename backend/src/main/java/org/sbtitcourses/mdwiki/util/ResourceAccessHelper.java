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

/**
 * Компонент для проверки доступа к ресурсам
 */
@Component
public class ResourceAccessHelper {

    /**
     * Репозиторий для взаимодействия с сущностью Person
     */
    private final PersonRepository personRepository;

    /**
     * Конструктор для автаматического внедрения зависимостей
     * @param personRepository репозиторий для взаимодействия с сущностью Person
     */
    @Autowired
    public ResourceAccessHelper(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Метод, проверяющий доступ к получению пространства
     * @param space пространство, доступ к которому нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToReadSpaceDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> space.isPublic() || person.getId() == space.getOwner().getId()).isEmpty();
    }

    /**
     * Метод, проверяющий доступ к изменению пространства
     * @param space пространство, доступ к которому нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToUpdateSpaceDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == space.getOwner().getId()).isEmpty();
    }

    /**
     * Метод, проверяющий доступ к удалению пространства
     * @param space пространство, доступ к которому нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToDeleteSpaceDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == space.getOwner().getId()).isEmpty();
    }

    /**
     * Метод, проверяющий доступ к созданию страницы
     * @param space пространство, в котором нужно создать страницу
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToCreatePageDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == space.getOwner().getId()).isEmpty();
        //TODO: this == access to update space (???)
    }

    /**
     * Метод, проверяющий доступ к созданию подстраницы
     * @param parent страница-родитель
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToCreateSubpageDenied(Page parent) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == parent.getOwner().getId()).isEmpty();
    }

    /**
     * Метод, проверяющий доступ к получению всех страниц
     * @param space пространство, из которого нужно получить все страницы
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToReadAllPagesDenied(Space space) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> space.isPublic() || person.getId() == space.getOwner().getId()).isEmpty();
    }

    /**
     * Метод, проверяющий доступ к получению страницы
     * @param page страница, доступ к которой нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
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

    /**
     * Метод, проверяющий доступ к получению корневой страницы
     * @param page страница, доступ к которой нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    private boolean isAccessToReadRootPageDenied(Page page) {
        if (page.getParent() != null) {
            return isAccessToReadRootPageDenied(page.getParent());
        }

        return !page.isPublic();
    }

    /**
     * Метод, проверяющий доступ к изменению страницы
     * @param page страница, доступ к которой нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToUpdatePageDenied(Page page) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == page.getOwner().getId()).isEmpty();
    }

    /**
     * Метод, проверяющий доступ к удалению страницы
     * @param page страница, доступ к которой нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToDeletePageDenied(Page page) {
        Optional<Person> user = getLoggedInUser();

        return user.filter(person -> person.getId() == page.getOwner().getId()).isEmpty();
    }

    /**
     * Метод, проверяющий доступ к созданию документа
     * @param page страница, в которой нужно создать документ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToCreateDocumentDenied(Page page) {
        return isAccessToCreatePageDenied(page.getSpace());
    }

    /**
     * Метод, проверяющий доступ к получению документа
     * @param document документ, доступ к которому нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToReadDocumentDenied(Document document) {
       return isAccessToReadPageDenied(document.getPage());
    }

    /**
     * Метод, проверяющий доступ к изменению документа
     * @param document документ, доступ к которому нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToUpdateDocumentDenied(Document document) {
        return isAccessToUpdatePageDenied(document.getPage());
    }

    /**
     * Метод, проверяющий доступ к удалению документа
     * @param document документ, доступ к которому нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    public boolean isAccessToDeleteDocumentDenied(Document document) {
        return isAccessToDeletePageDenied(document.getPage());
    }

    /**
     * Метод, отвечающий за получение объекта пользователя
     * @return Optional с объектом пользователя, если он прошел аутентифицию, пустой optional в противном случае
     */
    public Optional<Person> getLoggedInUser() {
        PersonDetails principal = (PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        return personRepository.findByUsername(username);
    }
}
