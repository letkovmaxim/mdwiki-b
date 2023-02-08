package org.sbtitcourses.mdwiki.repository;

import org.sbtitcourses.mdwiki.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для взаимодействия с сущностью {@link Person}.
 */
public interface PersonRepository extends JpaRepository<Person, Integer> {

    /**
     * Поиск пользователя по его логину.
     *
     * @param username логин пользоватлея.
     * @return найденного пользователя.
     */
    Optional<Person> findByUsername(String username);

    /**
     * Поиск пользователя по его эл. почте.
     *
     * @param email эл. почта пользователя.
     * @return найденного пользователя.
     */
    Optional<Person> findByEmail(String email);

    /**
     * Поиск пользователя по его логину или эл. почте.
     *
     * @param username логин пользователя.
     * @param email    эл. почта пользователя.
     * @return найденного пользователя.
     */
    Optional<Person> findByUsernameOrEmail(String username, String email);
}
