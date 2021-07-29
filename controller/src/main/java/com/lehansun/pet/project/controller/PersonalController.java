package com.lehansun.pet.project.controller;

import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Rest controller for work with authenticated customer.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class PersonalController {

    /**
     * Service layer object to get information about Customers.
     */
    private final CustomerService customerService;

    /**
     * Security layer object to work with JSON Web Tokens
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Finds authenticated customer.
     *
     * @return ResponseEntity which contains Customer DTOs.
     */
    @GetMapping
    public ResponseEntity<CustomerDTO> get(HttpServletRequest request) {
        log.debug("Received Get request: /me");

        String token = jwtTokenProvider.retrieveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        CustomerDTO dtoByUsername = customerService.getDtoByUsername(username);
        return ResponseEntity.ok(dtoByUsername);
    }

}
