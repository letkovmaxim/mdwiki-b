package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Сервис с логикой CRUD операций над сущностью Space
 */
@Service
@Transactional(readOnly = true)
public class SpaceService implements SpaceCrudService {

    /**
     * Репозиторий для взаимодействия с сущностью Space
     */
    private final SpaceRepository spaceRepository;

    /**
     * Конструктор для автоматичекого внедрения зависимостей
     * @param spaceRepository репозиторий для взаимодействия с сущностью Space
     */
    @Autowired
    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    /**
     * Метод, отвечающий за создание нового пространства в репозитории
     * @param spaceToSave пространство, которое нужно сохранить
     * @return сохраненное пространство
     */
    @Override
    @Transactional
    public Space create(Space spaceToSave) {
        Date now = new Date();
        spaceToSave.setCreatedAt(now);
        spaceToSave.setUpdatedAt(now);
        spaceToSave.setId(spaceRepository.save(spaceToSave).getId());

        return spaceToSave;
    }

    /**
     * Метод, отвечающий за получение всех пространств из репозитория
     * @return список всех пространств
     */
    @Override
    public List<Space> getAll() {
        return spaceRepository.findAll();
    }

    /**
     * Метод, отвечающий за получение пространства по его ID
     * @param id ID пространства
     * @return найденое пространтво
     * @throws ElementNotFoundException если пространства с таким ID не существует
     */
    @Override
    public Space get(int id) throws ElementNotFoundException {
        return spaceRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));
    }

    /**
     * Метод, отвечающий за обновление пространства по его ID
     * @param id ID пространства
     * @param spaceToUpdateWith пространство, значениями полей которого нужно обновить требуемое пространство
     * @return обновленное пространство
     * @throws ElementNotFoundException если пространства с таким ID не существует
     */
    @Override
    @Transactional
    public Space update(int id, Space spaceToUpdateWith) throws ElementNotFoundException {
        Space spaceToUpdate = spaceRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));
        spaceToUpdate.setName(spaceToUpdateWith.getName());
        spaceToUpdate.setPublic(spaceToUpdateWith.isPublic());

        spaceRepository.save(spaceToUpdate);

        return spaceToUpdate;
    }

    /**
     * Метод, отвечающий за удаление пространства по его ID
     * @param id ID пространства
     * @throws ElementNotFoundException если пространства с таким ID не существует
     */
    @Override
    @Transactional
    public void delete(int id) throws ElementNotFoundException {
        Space spaceToDelete = spaceRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));
        spaceRepository.delete(spaceToDelete);
    }
}