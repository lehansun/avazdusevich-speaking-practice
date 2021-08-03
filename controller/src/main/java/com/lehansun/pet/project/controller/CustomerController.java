package com.lehansun.pet.project.controller;

import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.model.dto.CustomerDtoWithPassword;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Rest controller for work with Customers.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    /**
     * Service layer object to get information about Customers.
     */
    private final CustomerService service;

    private final PasswordEncoder encoder;


    /**
     * Finds all customers.
     *
     * @return ResponseEntity which contains a List of all found Customer DTOs.
     */
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        log.debug("Received Get request: /customers");

        List<CustomerDTO> dtoList = service.getAllDTOs();
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Finds customer with given ID.
     *
     * @param id customer ID.
     * @return ResponseEntity which contains a looked for Customer DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        log.debug("Received Get request: /customers/" + id);
        return ResponseEntity.ok(service.getDtoById(id));
    }

    /**
     * Creates new customer.
     *
     * @param customerDTO customer.
     * @return ResponseEntity which contains a customer DTO with new assigned ID.
     */
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDtoWithPassword customerDTO) {
        log.info("Received Post request: /customers");
        customerDTO.setPassword(encoder.encode(customerDTO.getPassword()));
        return ResponseEntity.status(201).body(service.saveByDTO(customerDTO));
    }

    /**
     * Updates an existing customer.
     *
     * @param customerDTO an object containing fields to update.
     * @param id Id of customer to update
     * @return ResponseEntity with status 204.
     */
    @PatchMapping("/{id}")
    public  ResponseEntity<Void> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("id") long id) {
        log.info("Received patch request: /customers/" + id);
        service.updateByDTO(id, customerDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes customer from data source.
     *
     * @param id Id of customer to delete.
     * @return ResponseEntity with status 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") long id) {
        log.debug("Received delete request: /customers/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
