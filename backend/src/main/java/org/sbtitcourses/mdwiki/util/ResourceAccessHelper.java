package org.sbtitcourses.mdwiki.util;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;

/**
 * Класс для проверки доступа к ресурсам
 */
public final class ResourceAccessHelper {

    private ResourceAccessHelper() {}

    /**
     * Метод, проверяющий доступ к получению пространства
     * @param space пространство, доступ к которому нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToReadSpaceDenied(Space space, Person user) {
        return !space.isShared() && user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий доступ к изменению пространства
     * @param space пространство, доступ к которому нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToUpdateSpaceDenied(Space space, Person user) {
        return user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий доступ к удалению пространства
     * @param space пространство, доступ к которому нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToDeleteSpaceDenied(Space space, Person user) {
        return user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий доступ к созданию страницы
     * @param space пространство, в котором нужно создать страницу
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToCreatePageDenied(Space space, Person user) {
        return user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий доступ к созданию подстраницы
     * @param parent страница-родитель
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToCreateSubpageDenied(Page parent, Person user) {
        return user.getId() != parent.getOwner().getId();
    }

    /**
     * Метод, проверяющий доступ к получению всех страниц
     * @param space пространство, из которого нужно получить все страницы
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToReadAllPagesDenied(Space space, Person user) {
        return !space.isShared() && user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий доступ к получению страницы
     * @param page страница, доступ к которой нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToReadPageDenied(Page page, Person user) {
        if (user.getId() == page.getOwner().getId() || page.getSpace().isShared()) {
            return false;
        }

        return isAccessToReadRootPageDenied(page);
}

    /**
     * Метод, проверяющий доступ к получению корневой страницы
     * @param page страница, доступ к которой нужно проверить
     * @return true - если доспут запрещен, false - если разрешен
     */
    private static boolean isAccessToReadRootPageDenied(Page page) {
        if (page.getParent() != null) {
            return isAccessToReadRootPageDenied(page.getParent());
        }

        return !page.isShared();
    }

    /**
     * Метод, проверяющий доступ к изменению страницы
     * @param page страница, доступ к которой нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToUpdatePageDenied(Page page, Person user) {
        return user.getId() != page.getOwner().getId();
    }

    /**
     * Метод, проверяющий доступ к удалению страницы
     * @param page страница, доступ к которой нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToDeletePageDenied(Page page, Person user) {
        return user.getId() != page.getOwner().getId();
    }

    /**
     * Метод, проверяющий доступ к созданию документа
     * @param page страница, в которой нужно создать документ
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToCreateDocumentDenied(Page page, Person user) {
        return isAccessToCreatePageDenied(page.getSpace(), user);
    }

    /**
     * Метод, проверяющий доступ к получению документа
     * @param document документ, доступ к которому нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToReadDocumentDenied(Document document, Person user) {
       return isAccessToReadPageDenied(document.getPage(), user);
    }

    /**
     * Метод, проверяющий доступ к изменению документа
     * @param document документ, доступ к которому нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToUpdateDocumentDenied(Document document, Person user) {
        return isAccessToUpdatePageDenied(document.getPage(), user);
    }

    /**
     * Метод, проверяющий доступ к удалению документа
     * @param document документ, доступ к которому нужно проверить
     * @param user пользователь, для которого проверяется доступ
     * @return true - если доспут запрещен, false - если разрешен
     */
    public static boolean isAccessToDeleteDocumentDenied(Document document, Person user) {
        return isAccessToDeletePageDenied(document.getPage(), user);
    }
}
