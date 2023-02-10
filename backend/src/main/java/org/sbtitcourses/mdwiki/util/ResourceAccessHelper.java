package org.sbtitcourses.mdwiki.util;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;

/**
 * Вспомогательный класс, содержащий методы
 * для проверки доступа пользователя к ресурсам.
 */
public final class ResourceAccessHelper {

    private ResourceAccessHelper() {
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к чтению пространства.
     * Доступ разрешен, если пользователь - владелец этого пространства,
     * или пространство является публичным.
     *
     * @param space пространство, доступ к которому нужно проверить.
     * @param user  пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToReadSpaceDenied(Space space, Person user) {
        return !space.isShared() && user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к изменению пространства.
     * Доступ разрешен, если пользователь - владелец этого пространства.
     *
     * @param space пространство, доступ к которому нужно проверить.
     * @param user  пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToUpdateSpaceDenied(Space space, Person user) {
        return user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к удалению пространства.
     * Доступ разрешен, если пользователь - владелец этого пространства.
     *
     * @param space пространство, доступ к которому нужно проверить.
     * @param user  пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToDeleteSpaceDenied(Space space, Person user) {
        return user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к созданию страницы в пространстве.
     * Доступ разрешен, если пользователь - владелец этого пространства.
     *
     * @param space пространство, в котором нужно создать страницу.
     * @param user  пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToCreatePageDenied(Space space, Person user) {
        return user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к созданию подстраницы.
     * Доступ разрешен, если пользователь - владелец страницы-родителя.
     *
     * @param parent страница-родитель.
     * @param user   пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToCreateSubpageDenied(Page parent, Person user) {
        return user.getId() != parent.getOwner().getId();
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к чтению всех страниц пространства.
     * Доступ разрешен, если пользователь - владелец этого пространства,
     * или пространство является публичным.
     *
     * @param space пространство, из которого нужно получить все страницы.
     * @param user  пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToReadAllPagesDenied(Space space, Person user) {
        return !space.isShared() && user.getId() != space.getOwner().getId();
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к чтению страницы.
     * Доступ разрешен, если пользователь - владелец этой страницы,
     * или пространство является публичным, при условии, что разрешен доступ
     * к чтению её страницы-родителя.
     *
     * @param page страница, доступ к которой нужно проверить.
     * @param user пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToReadPageDenied(Page page, Person user) {
        if (user.getId() == page.getOwner().getId()) {
            return false;
        }

        return !page.isShared() || isAccessToReadParentPagesDenied(page);
    }

    /**
     * Метод, рекурсиво проверяющий запрещен ли пользователю доступ к чтению страниц-родителей.
     * Доступ разрешен, если в дереве нет приватных страниц
     * и пространство, в котором находятся страницы, является публичным.
     *
     * @param page страница, доступ к которой нужно проверить.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    private static boolean isAccessToReadParentPagesDenied(Page page) {
        if (page.getParent() == null) {
            return !page.isShared() || !page.getSpace().isShared();
        }

        return !page.isShared() || isAccessToReadParentPagesDenied(page.getParent());
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к изменению страницы.
     * Доступ разрешен, если пользователь - владелец этой страницы.
     *
     * @param page страница, доступ к которой нужно проверить.
     * @param user пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToUpdatePageDenied(Page page, Person user) {
        return user.getId() != page.getOwner().getId();
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к удалению страницы.
     * Доступ разрешен, если пользователь - владелец этой страницы.
     *
     * @param page страница, доступ к которой нужно проверить.
     * @param user пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToDeletePageDenied(Page page, Person user) {
        return user.getId() != page.getOwner().getId();
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к созданию документа.
     * Доступ разрешен, если разрешен доступ к созданию страницы.
     *
     * @param page страница, в которой нужно создать документ.
     * @param user пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToCreateDocumentDenied(Page page, Person user) {
        return isAccessToCreatePageDenied(page.getSpace(), user);
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к чтению документа.
     * Доступ разрешен, если разрешен доступ к чтению страницы.
     *
     * @param document документ, доступ к которому нужно проверить.
     * @param user     пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToReadDocumentDenied(Document document, Person user) {
        return isAccessToReadPageDenied(document.getPage(), user);
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к изменению документа.
     * Доступ разрешен, если разрешен доступ к изменению страницы.
     *
     * @param document документ, доступ к которому нужно проверить.
     * @param user     пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToUpdateDocumentDenied(Document document, Person user) {
        return isAccessToUpdatePageDenied(document.getPage(), user);
    }

    /**
     * Метод, проверяющий запрещен ли пользователю доступ к удалению документа.
     * Доступ разрешен, если разрешен доступ к удалению страницы.
     *
     * @param document документ, доступ к которому нужно проверить.
     * @param user     пользователь, для которого проверяется доступ.
     * @return true - если доспут запрещен, false - если разрешен.
     */
    public static boolean isAccessToDeleteDocumentDenied(Document document, Person user) {
        return isAccessToDeletePageDenied(document.getPage(), user);
    }
}