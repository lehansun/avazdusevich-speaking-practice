package com.lehansun.pet.project.controller;

import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.api.service.RequestService;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.model.dto.RequestDTO;
import com.lehansun.pet.project.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * Service layer object to get information about Requests.
     */
    private final RequestService requestService;

    /**
     * Security layer object to work with JSON Web Tokens
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Finds authenticated customer.
     *
     * @param request HTTP request
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

    /**
     * Finds list of requests initiated by authenticated customer.
     *
     * @param request HTTP request
     * @return ResponseEntity which contains list of request DTOs.
     */
    @GetMapping("/requests")
    public ResponseEntity<List<RequestDTO>> getRequests(HttpServletRequest request) {
        log.debug("Received Get request: /me/requests");

        String token = jwtTokenProvider.retrieveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        List<RequestDTO> requestDTOs = requestService.getDTOsInitiatedBy(username);
        return ResponseEntity.ok(requestDTOs);
    }

}
