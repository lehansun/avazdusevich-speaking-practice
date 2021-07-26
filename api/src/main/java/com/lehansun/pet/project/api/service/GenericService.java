package com.lehansun.pet.project.api.service;

import com.lehansun.pet.project.model.BaseEntity;

import java.util.List;
import java.util.Optional;

/**
 * A simple generic Service interface that defines the methods
 * of working with the BaseEntity model inheritors.
 * A single instance implementing this interface can be used for
 * multiple types of domain objects.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
public interface GenericService<T extends BaseEntity> {

    /**
     * Finds all entities.
     *
     * @return entities list.
     */
    List<T> getAll();

    /**
     * Finds entity by Id.
     *
     * @param id entity Id.
     * @return entity.
     */
    Optional<T> getById(Long id);

    /**
     * Save new entity.
     *
     * @param element entity to save.
     */
    void save(T element);

    /**
     * Updates entity.
     *
     * @param element entity to update.
     */
    void update(T element);

    /**
     * Deletes entity.
     *
     * @param element entity to delete.
     */
    void delete(T element);

    /**
     * Deletes entity by Id.
     *
     * @param id Id of entity to delete.
     */
    void delete(Long id);
}
