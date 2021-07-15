package com.lehansun.pet.project.api.dao;

import com.lehansun.pet.project.model.Customer;

import java.util.Optional;

/**
 * A simple  DAO interface to handle the database operations
 * required to manipulate the Customer model.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
public interface CustomerDao extends GenericDao<Customer>{

    Optional<Customer> getByUsername(String username);
}
