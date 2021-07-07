package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.GenericDao;
import com.lehansun.pet.project.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * This class contains methods for direct access
 * to data source using Spring JPA.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
public abstract class AbstractJpaDao<T extends BaseEntity> implements GenericDao<T> {

    public static final String SAVE_LOG_MESSAGE = "IN save(), entity - {}";
    public static final String GET_ALL_LOG_MESSAGE = "IN getAll(), entities - {}";
    public static final String GET_BY_ID_LOG_MESSAGE = "IN getById(), entity - {}, id - {}";
    public static final String UPDATE_LOG_MESSAGE = "IN update(), entity - {}, id - {}";
    public static final String DELETE_LOG_MESSAGE = "IN delete(), entity - {}, id - {}";

    /**
     * Interface used to interact with the persistence context.
     */
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    protected EntityManager entityManager;

    /**
     * Save new entity.
     *
     * @param entity entity.
     */
    @Override
    public void save(T entity) {
        log.debug(SAVE_LOG_MESSAGE, getClazz().getSimpleName());
        entityManager.persist(entity);
    }

    /**
     * Finds all entities.
     *
     * @return entities list.
     */
    @Override
    public List<T> getAll() {
        log.debug(GET_ALL_LOG_MESSAGE, getClazz().getSimpleName());
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(getClazz());
        Root<T> root = criteriaQuery.from(getClazz());
        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Finds entity by Id.
     *
     * @param id entity Id.
     * @return entity.
     */
    @Override
    public Optional<T> getById(Long id) {
        log.debug(GET_BY_ID_LOG_MESSAGE, getClazz().getSimpleName(), id);
        return Optional.ofNullable(entityManager.find(getClazz(), id));
    }

    /**
     * Updates entity.
     *
     * @param entity entity.
     * @return updated entity.
     */
    @Override
    public T update(T entity) {
        log.debug(UPDATE_LOG_MESSAGE, getClazz().getSimpleName(), entity.getId());
        entityManager.merge(entity);
        return entity;
    }

    /**
     * Deletes entity.
     *
     * @param entity entity.
     */
    @Override
    public void delete(T entity) {
        log.debug(DELETE_LOG_MESSAGE, getClazz().getSimpleName(), entity.getId());
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    /**
     * Defines the class of dependent entity.
     *
     * @return dependent entity class.
     */
    protected abstract Class<T> getClazz();
}
