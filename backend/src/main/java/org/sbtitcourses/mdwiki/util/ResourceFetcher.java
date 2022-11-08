package org.sbtitcourses.mdwiki.util;

import org.sbtitcourses.mdwiki.model.Document;
import org.sbtitcourses.mdwiki.model.Page;
import org.sbtitcourses.mdwiki.model.Space;
import org.sbtitcourses.mdwiki.repository.DocumentRepository;
import org.sbtitcourses.mdwiki.repository.PageRepository;
import org.sbtitcourses.mdwiki.repository.SpaceRepository;
import org.sbtitcourses.mdwiki.util.exception.ElementNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceFetcher {

    private final SpaceRepository spaceRepository;
    private final PageRepository pageRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public ResourceFetcher(SpaceRepository spaceRepository, PageRepository pageRepository, DocumentRepository documentRepository) {
        this.spaceRepository = spaceRepository;
        this.pageRepository = pageRepository;
        this.documentRepository = documentRepository;
    }

    public Space fetchSpace(int spaceId) throws ElementNotFoundException {
        return spaceRepository.findById(spaceId)
                .orElseThrow(() -> new ElementNotFoundException("Пространство не найдено"));
    }

    public Page fetchPage(int pageId, int spaceId) throws ElementNotFoundException {
        Space space = fetchSpace(spaceId);

        return pageRepository.findByIdAndSpace(pageId, space)
                .orElseThrow(() -> new ElementNotFoundException("Страница не найдена"));
    }

    public Document fetchDocument(int pageId, int spaceId) throws ElementNotFoundException {
        Page page = fetchPage(pageId, spaceId);

        return documentRepository.findByPage(page)
                .orElseThrow(() -> new ElementNotFoundException("Документ не найден"));
    }
}
