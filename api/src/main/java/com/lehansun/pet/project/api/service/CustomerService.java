package com.lehansun.pet.project.api.service;

import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.model.dto.ExtendedSecureCustomerDTO;

import java.util.List;

/**
 * A service interface that defines the methods
 * of working with the Customer model.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
public interface CustomerService extends GenericService<Customer> {

    /**
     * Finds all customers.
     *
     * @return list of customer DTOs.
     */
    List<CustomerDTO> getAllDTOs();

    /**
     * Finds customer by Id.
     *
     * @param id customer Id.
     * @return customer DTO.
     */
    CustomerDTO getDtoById(long id);

    /**
     * Finds customer by username.
     *
     * @param username customer username.
     * @return customer DTO.
     */
    CustomerDTO getDtoByUsername(String username);

    /**
     * Creates and save new customer.
     *
     * @param customerDTO customer Data Transfer Object.
     * @return customerDTO with new assigned ID.
     */
    CustomerDTO saveByDTO(CustomerDTO customerDTO);

    /**
     * Updates customer.
     *
     * @param id id of customer to update.
     * @param customerDTO customer DTO containing fields to update.
     */
    void updateByDTO(long id, CustomerDTO customerDTO);
}
