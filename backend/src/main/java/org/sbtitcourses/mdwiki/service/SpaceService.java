package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.exception.SpaceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Space create(Space entity) {
        return null;
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
    public Space update(int id, Space updatedSpace) {
        Space spaceToUpdate = spaceRepository.findById(id).orElseThrow(SpaceNotFoundException::new);

        spaceToUpdate.setName(updatedSpace.getName());

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
