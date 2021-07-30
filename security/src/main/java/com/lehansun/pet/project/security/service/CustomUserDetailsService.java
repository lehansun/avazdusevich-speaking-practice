package com.lehansun.pet.project.security.service;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Role;
import com.lehansun.pet.project.security.model.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A service class which provides interaction
 * with the Customer model and map them to UserDetails objects.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    public static final String CUSTOMER_DON_T_EXIST_FORMATTER = "Customer don't exist. Username - %s.";

    /**
     * A Customer data access object.
     */
    private final CustomerDao customerDao;

    /**
     * Finds customer by username.
     *
     * @param username customer's username.
     * @return UserDetails object.
     * @throws UsernameNotFoundException if customer doesn't exist.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> optionalCustomer = customerDao.getByUsername(username);
        if (optionalCustomer.isPresent()) {
            return fromDto(optionalCustomer.get());
        } else {
            String message = String.format(CUSTOMER_DON_T_EXIST_FORMATTER, username);
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Mapping from Customer class to UserDetails class.
     *
     * @param customer mapped customer.
     * @return UserDetails object.
     */
    private UserDetails fromDto(Customer customer) {
        SecurityUser securityUser = new SecurityUser();
        securityUser.setUsername(customer.getUsername());
        securityUser.setPassword(customer.getPassword());
        securityUser.setRoles(customer.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet()));
        return securityUser;
    }
}
