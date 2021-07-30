package com.lehansun.pet.project.api.dao;

import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Request;

import java.time.LocalDate;
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
     * Finds all requests initiated by certain customer for the specified period.
     *
     * @param customer customer who initiated requests.
     * @param dateFrom period start date.
     * @param dateTo period end date.
     * @param isAccepted the parameter displays whether the request should be accepted or not.
     * @return list of requests.
     */
    List<Request> getByInitiator(Customer customer, LocalDate dateFrom, LocalDate dateTo, Boolean isAccepted);
}
