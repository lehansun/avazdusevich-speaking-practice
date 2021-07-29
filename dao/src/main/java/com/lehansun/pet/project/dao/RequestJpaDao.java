package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * This class contains methods for direct access to data source using
 * Spring JPA. The object of this class allows to manipulate the
 * models of the Request.class
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Repository
public class RequestJpaDao extends AbstractJpaDao<Request> implements RequestDao {

    /**
     * Finds all requests initiated by certain customer.
     *
     * @param customer customer who initiated requests.
     * @return request list.
     */
    @Override
    public List<Request> getByInitiator(Customer customer) {
        log.debug("IN getByInitiator()");

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> root = criteriaQuery.from(Request.class);
        CriteriaQuery<Request> query = criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("initiatedBy"), customer));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Defines the class of manipulated entity.
     *
     * @return Request.class.
     */
    @Override
    protected Class<Request> getClazz() {
        return Request.class;
    }

}
