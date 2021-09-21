package com.lehansun.pet.project.speakingpractice.service.impl;

import com.lehansun.pet.project.speakingpractice.model.BaseEntity;
import com.lehansun.pet.project.speakingpractice.service.GenericService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
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
public abstract class AbstractService<T extends BaseEntity> implements GenericService<T> {

    protected static final String ELEMENT_WITH_NON_EXISTENT_ID = "Element with id:%d does not exist";
    protected static final String ELEMENT_WITH_NON_EXISTENT_USERNAME = "Element with username-%s does not exist";
    protected static final String FAILED_TO_UPDATE_ELEMENT = "Failed to update element-";
    private static final String FAILED_TO_DELETE_ELEMENT_BY_ID = "Failed to delete element id-";

    /**
     * A data access object.
     */
    protected final JpaRepository<T, Long> repository;

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
        return repository.findAll();
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
        return repository.findById(id);
    }

    /**
     * Save new entity.
     *
     * @param element entity to save.
     */
    @Override
    public void save(T element) {
        log.debug("IN save({})", element);
        repository.save(element);
    }

    /**
     * Deletes entity.
     *
     * @param element entity to delete.
     */
    @Override
    public void delete(T element) {
        log.debug("IN delete({})", element);
        repository.delete(element);
    }

    /**
     * Deletes entity by Id.
     *
     * @param id Id of entity to delete.
     */
    @Override
    public void delete(Long id) {
        log.debug("IN delete({})", id);
        Optional<T> byId = repository.findById(id);
        if (byId.isPresent()) {
            repository.delete(byId.get());
        } else {
            log.warn(FAILED_TO_DELETE_ELEMENT_BY_ID + id);
            throw new RuntimeException(FAILED_TO_DELETE_ELEMENT_BY_ID + id);
        }
    }
}
