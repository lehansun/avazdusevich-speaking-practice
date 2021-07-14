package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.model.Customer;
import org.springframework.stereotype.Repository;

/**
 * This class contains methods for direct access to data source using
 * Spring JPA. The object of this class allows to manipulate the
 * models of the Customer.class
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Repository
public class CustomerJpaDao extends AbstractJpaDao<Customer> implements CustomerDao {

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
    public Customer getByUsername(String username) {
        return entityManager.find(getClazz(), username);
    }
}
