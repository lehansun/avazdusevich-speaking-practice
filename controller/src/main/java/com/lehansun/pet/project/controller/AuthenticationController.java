package com.lehansun.pet.project.controller;

import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.model.dto.ExtendedSecureCustomerDTO;
import com.lehansun.pet.project.model.dto.SecureCustomerDTO;
import com.lehansun.pet.project.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Rest controller for work with authentication.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/speaking-practice/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";

    /**
     * An object processing an Authentication requests.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Service layer object to get information about Customers.
     */
    private final CustomerService customerService;

    /**
     * Security layer object for creating and verifying JSON Web Tokens
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Finds the specified customer in the database.
     * If the customer is found, the method authenticates him and creates a JSON Web Token.
     *
     * @param request customer DTO containing required for authentication username and password
     * @return ResponseEntity which contains a JSON Web Token.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SecureCustomerDTO request) {
        try {
            log.debug("IN login. Get request: {}", request);
            ExtendedSecureCustomerDTO byUsername = customerService.getDtoByUsername(request.getUsername());
            log.debug("IN login. Get customer from request: {}", byUsername);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            log.debug("IN login. Authentication created: {}", authentication);
            String token = jwtTokenProvider.createToken(request.getUsername(), byUsername.getRoles().get(0).getName());
            log.debug("IN login. jwtToken created: {}", token);

            Map<Object, Object> response = new HashMap<>();
            response.put("username", request.getUsername());
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(INVALID_USERNAME_OR_PASSWORD, HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Logout a user from the system
     *
     * @param request request
     * @param response response
     */
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

}
