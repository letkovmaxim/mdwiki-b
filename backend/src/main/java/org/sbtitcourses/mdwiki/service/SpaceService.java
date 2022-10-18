package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Person;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.exception.SpaceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class SpaceService implements CrudService<Space> {

    private final SpaceRepository spaceRepository;

    @Autowired
    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @Override
    @Transactional
    public Space create(Space spaceToSave) {
        Date now = new Date();
        spaceToSave.setCreatedAt(now);
        spaceToSave.setUpdatedAt(now);
        spaceToSave.setId(spaceRepository.save(spaceToSave).getId());

        return spaceToSave;
    }

    @Override
    public List<Space> getAll() {
        return spaceRepository.findAll();
    }

    @Override
    public Space get(int id) {
        return spaceRepository.findById(id).orElseThrow(SpaceNotFoundException::new);
    }

    @Override
    @Transactional
    public Space update(int id, Space spaceToUpdateWith) {
        Space spaceToUpdate = spaceRepository.findById(id).orElseThrow(SpaceNotFoundException::new);
        spaceToUpdate.setName(spaceToUpdateWith.getName());
        spaceToUpdate.setPublic(spaceToUpdateWith.getPublic());

        spaceRepository.save(spaceToUpdate);

        return spaceToUpdate;
    }

    @Override
    @Transactional
    public void delete(int id) {
        Space spaceToDelete = spaceRepository.findById(id).orElseThrow(SpaceNotFoundException::new);
        spaceRepository.deleteById(spaceToDelete.getId());
    }
}
