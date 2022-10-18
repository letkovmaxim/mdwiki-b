package org.sbtitcourses.mdwiki.service;

import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.util.exception.PageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PageService implements CrudService<Page> {

    private final PageRepository pageRepository;

    @Autowired
    public PageService(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @Override
    @Transactional
    public Page create(Page pageToCreate) {
        Date now = new Date();
        pageToCreate.setCreatedAt(now);
        pageToCreate.setUpdatedAt(now);
        pageToCreate.setId(pageRepository.save(pageToCreate).getId());

        return pageToCreate;
    }

    @Override
    public List<Page> getAll() {
        return pageRepository.findByParentIsNull();
    }

    public List<Page> getAll(Space space) {
        return pageRepository.findBySpaceAndParentIsNull(space);
    }

    @Override
    public Page get(int id) {
        return pageRepository.findById(id).orElseThrow(PageNotFoundException::new);
    }

    public Page get(int id, Space space) {
        return pageRepository.findByIdAndSpace(id, space).orElseThrow(PageNotFoundException::new);
    }

    @Override
    @Transactional
    public Page update(int id, Page pageToUpdateWith) {
        Page pageToUpdate = pageRepository.findById(id).orElseThrow(PageNotFoundException::new);
        pageToUpdate.setName(pageToUpdateWith.getName());
        pageToUpdate.setPublic(pageToUpdateWith.getPublic());

        pageRepository.save(pageToUpdate);

        return pageToUpdate;
    }

    @Override
    @Transactional
    public void delete(int id) {
        Page pageToDelete = pageRepository.findById(id).orElseThrow(PageNotFoundException::new);
        pageRepository.deleteById(pageToDelete.getId());
    }
}
