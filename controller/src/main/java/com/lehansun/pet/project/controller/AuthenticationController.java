package com.lehansun.pet.project.controller;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.model.Customer;
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
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private static final String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";

    private final AuthenticationManager authenticationManager;
    private final CustomerDao customerDao;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SecureCustomerDTO request) {
        try {
            log.debug("IN login. Get request: {}", request);
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            log.debug("IN login. Authentication created: {}", authentication);
            Optional<Customer> optionalCustomer = customerDao.getByUsername(request.getUsername());
            if (optionalCustomer.isEmpty()) {
                log.warn("IN login. Failed to get customer from request.");
                throw new RuntimeException("User doesn't exist");
            }
            Customer customer = optionalCustomer.get();
            System.out.println(customer.getRoles());
            log.debug("IN login. Get customer from request: {}", customer);
            String token = jwtTokenProvider.createToken(request.getUsername(), customer.getRoles().get(0).getName());
            log.debug("IN login. jwtToken created: {}", token);
            Map<Object, Object> response = new HashMap<>();
            response.put("username", request.getUsername());
            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>(INVALID_USERNAME_OR_PASSWORD, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

}
