package com.lehansun.pet.project.controller;

import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.api.service.RequestService;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.model.dto.PasswordDTO;
import com.lehansun.pet.project.model.dto.RequestDTO;
import com.lehansun.pet.project.security.JwtPasswordValidator;
import com.lehansun.pet.project.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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
     * Service object to password work
     */
    private final JwtPasswordValidator passwordValidator;

    /**
     * Finds authenticated customer.
     *
     * @param request HTTP request
     * @return ResponseEntity which contains Customer DTOs.
     */
    @GetMapping
    public ResponseEntity<CustomerDTO> get(HttpServletRequest request) {
        log.debug("Received Get request: /me");

        String username = getUsername(request);
        CustomerDTO dtoByUsername = customerService.getDtoByUsername(username);
        return ResponseEntity.ok(dtoByUsername);
    }

    /**
     * Finds list of requests initiated by authenticated customer.
     *
     * @param request HTTP request
     * @param dateFrom period start date.
     * @param dateTo period finish date.
     * @param accepted the parameter displays whether the request should be accepted or not.
     * @return ResponseEntity which contains list of request DTOs.
     */
    @GetMapping("/requests")
    public ResponseEntity<List<RequestDTO>> getRequests(
            @RequestParam(value = "dateFrom", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate dateTo,
            @RequestParam(value = "accepted", required = false)
                    Boolean accepted,
            HttpServletRequest request) {
        log.debug("Received Get request: /me/requests");

        String username = getUsername(request);
        List<RequestDTO> requestDTOs;
        requestDTOs = requestService.getDTOsInitiatedBy(username, dateFrom, dateTo, accepted);
        return ResponseEntity.ok(requestDTOs);
    }

    /**
     * Finds list of requests.
     *
     * @param request HTTP request
     * @param dateFrom period start date.
     * @param dateTo period finish date.
     * @param language the name of requested language.
     * @return ResponseEntity which contains list of request DTOs.
     */
    @GetMapping("/find/requests")
    public ResponseEntity<List<RequestDTO>> findRequests(
            @RequestParam(value = "dateFrom", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd")
                    LocalDate dateTo,
            @RequestParam(value = "language", required = false)
                    String language,
            HttpServletRequest request) {
        log.debug("Received Get request: /me/find/requests");

        String username = getUsername(request);
        List<RequestDTO> requestDTOs = requestService.getOtherCustomersRequestDTOs(username, dateFrom, dateTo, language);
        return ResponseEntity.ok(requestDTOs);
    }

    /**
     * Updates the customer's password.
     *
     * @param request HTTP request.
     * @param passwordDTO DTO containing passwords.
     * @return status 202 if password successfully updated or 400 if not.
     */
    @PutMapping("/password")
    public ResponseEntity<Boolean> changePassword(HttpServletRequest request, @RequestBody PasswordDTO passwordDTO) {
        log.info("Received put request: /me/password");
        String username = getUsername(request);
        if (passwordValidator.validatePassword(username, passwordDTO)) {
            String newPassword = passwordValidator.encode(passwordDTO.getNewPassword());
            customerService.updatePassword(username, newPassword);
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * Extracts customers username from HTTP request
     *
     * @param request HTTP request
     * @return username
     */
    private String getUsername(HttpServletRequest request) {
        String token = jwtTokenProvider.retrieveToken(request);
        return jwtTokenProvider.getUsername(token);
    }

}
