package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
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

    private static final String GET_BY_USERNAME_LOG_MESSAGE = "IN getByUsername(), username - {}";
    public static final String INCORRECT_USERNAME_FORMATTER = "Incorrect username-%s";
    public static final String UPDATE_DEBUG_MESSAGE = "IN updatePassword(). Trying to update {}'s password.";

    /**
     * Defines the class of manipulated entity.
     *
     * @return Customer.class.
     */
    @Override
    protected Class<Customer> getClazz() {
        return Customer.class;
    }

    /**
     * Finds customer by username.
     *
     * @param username customer's username.
     * @return customer.
     */
    @Override
    public Optional<Customer> getByUsername(String username) {
        log.debug(GET_BY_USERNAME_LOG_MESSAGE, username);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> root = criteriaQuery.from(Customer.class);
        CriteriaQuery<Customer> query = criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), username));
        List<Customer> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    /**
     * Finds customer by username and updates his password.
     *
     * @param username customer's username.
     * @param newPassword new password.
     */
    @Override
    public void updatePassword(String username, String newPassword) {
        log.debug(UPDATE_DEBUG_MESSAGE, username);
        Optional<Customer> byUsername = getByUsername(username);
        if (byUsername.isPresent()) {
            byUsername.get().setPassword(newPassword);
            update(byUsername.get());
        } else {
            String message = String.format(INCORRECT_USERNAME_FORMATTER, username);
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }
}
