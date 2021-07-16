package com.lehansun.pet.project.controller;

import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        log.debug("Received Get request: /customers");

        List<CustomerDTO> dtoList = service.getAllDTOs();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getById(@PathVariable Long id) {
        log.debug("Received Get request: /customers/" + id);
        return ResponseEntity.ok(service.getDtoById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {
        log.info("Received Post request: /customers");
        return ResponseEntity.ok(service.saveDto(customer));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<Void> updateCustomer(@RequestBody CustomerDTO customer, @PathVariable("id") long id) {
        log.info("Received patch request: /customers/" + id);
        service.updateDto(id, customer);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable("id") long id) {
        log.debug("Received delete request: /customers/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
