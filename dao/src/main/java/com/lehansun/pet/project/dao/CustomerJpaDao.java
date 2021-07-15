package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

/**
 * This class contains methods for direct access to data source using
 * Spring JPA. The object of this class allows to manipulate the
 * models of the Customer.class
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Repository
public class CustomerJpaDao extends AbstractJpaDao<Customer> implements CustomerDao {

    private static final String GET_BY_USERNAME_LOG_MESSAGE = "IN getByUsername(), entity - {}, username - {}";

    /**
     * Defines the class of manipulated entity.
     *
     * @return dependent entity class.
     */
    @Override
    protected Class<Customer> getClazz() {
        return Customer.class;
    }

    @Override
    public Optional<Customer> getByUsername(String username) {
        log.debug(GET_BY_USERNAME_LOG_MESSAGE, getClazz().getSimpleName(), username);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), username));

        return Optional.ofNullable(entityManager.createQuery(criteriaQuery).getResultList().get(0));
    }
}
