package com.lehansun.pet.project.api.dao;

import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Request;

import java.util.List;

/**
 * A simple  DAO interface to handle the database operations
 * required to manipulate the Request model.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
public interface RequestDao extends GenericDao<Request> {

    /**
     * Finds all requests initiated by certain customer.
     *
     * @param customer customer who initiated requests.
     * @return request list.
     */
    List<Request> getByInitiator(Customer customer);
}
