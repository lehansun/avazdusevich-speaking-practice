package com.lehansun.pet.project.api.dao;

import com.lehansun.pet.project.model.BaseEntity;

import java.util.List;
import java.util.Optional;

/**
 * A simple generic DAO interface to handle the database operations
 * required to manipulate a BaseEntity model inheritors.
 * A single instance implementing this interface can be used for
 * multiple types of domain objects.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
public interface GenericDao<T extends BaseEntity> {

    /**
     * Save new entity.
     *
     * @param entity entity.
     */
    void save(T entity);

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
     * Updates entity.
     *
     * @param entity entity.
     * @return updated entity.
     */
    T update(T entity);


    /**
     * Deletes entity.
     *
     * @param entity entity.
     */
    void delete(T entity);
}
