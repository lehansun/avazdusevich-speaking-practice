package com.lehansun.pet.project.api.service;

import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.dto.CustomerDTO;

import java.util.List;


public interface CustomerService extends GenericService<Customer> {

    List<CustomerDTO> getAllDTOs();
    CustomerDTO getDtoById(long id);
    CustomerDTO saveDto(CustomerDTO customerDTO);
    CustomerDTO getDtoByUsername(String username);
    void updateDto(long id, CustomerDTO customerDTO);
}
