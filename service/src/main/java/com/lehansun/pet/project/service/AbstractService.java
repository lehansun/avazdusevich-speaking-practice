package com.lehansun.pet.project.service;

import com.lehansun.pet.project.api.dao.GenericDao;
import com.lehansun.pet.project.api.service.GenericService;
import com.lehansun.pet.project.model.BaseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * An abstract service class which provides default interaction
 * with the BaseEntity class inheritors.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AbstractService<T extends BaseEntity> implements GenericService<T> {

    protected static final String ELEMENT_WITH_NON_EXISTENT_ID = "Element with id-%d does not exist";
    protected static final String ELEMENT_WITH_NON_EXISTENT_USERNAME = "Element with username-%s does not exist";
    protected static final String FAILED_TO_UPDATE_ELEMENT = "Failed to update element-";
    private static final String FAILED_TO_DELETE_ELEMENT_BY_ID = "Failed to delete element id-";

    /**
     * A data access object.
     */
    protected final GenericDao<T> dao;

    /**
     * The ModelMapper object makes it easy to convert one model to another,
     * allowing separate models to remain segregated.
     */
    protected final ModelMapper modelMapper;

    /**
     * Finds all entities.
     *
     * @return entities list.
     */
    @Override
    public List<T> getAll() {
        log.debug("IN getAll()");
        return dao.getAll();
    }

    /**
     * Finds entity by Id.
     *
     * @param id entity Id.
     * @return entity.
     */
    @Override
    public Optional<T> getById(Long id) {
        log.debug("IN getById({})", id);
        return dao.getById(id);
    }

    /**
     * Save new entity.
     *
     * @param element entity.
     */
    @Override
    public void save(T element) {
        log.debug("IN save({})", element);
        dao.save(element);
    }

    /**
     * Updates entity.
     *
     * @param element entity.
     */
    @Override
    public void update(T element) {
        log.debug("IN update({})", element);
        dao.update(element);
    }

    /**
     * Deletes entity.
     *
     * @param element entity.
     */
    @Override
    public void delete(T element) {
        log.debug("IN delete({})", element);
        dao.delete(element);
    }

    /**
     * Deletes entity by Id.
     *
     * @param id entity Id.
     */
    @Override
    public void delete(Long id) {
        log.debug("IN delete({})", id);
        Optional<T> byId = dao.getById(id);
        if (byId.isPresent()) {
            dao.delete(byId.get());
        } else {
            log.warn(FAILED_TO_DELETE_ELEMENT_BY_ID + id);
            throw new RuntimeException(FAILED_TO_DELETE_ELEMENT_BY_ID + id);
        }
    }
}
