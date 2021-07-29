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

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerDao customerDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> optionalCustomer = customerDao.getByUsername(username);
        if (optionalCustomer.isPresent()) {
            return fromDto(optionalCustomer.get());
        } else {
            log.warn("Customer don't exist. Username - {}.", username);
            throw new RuntimeException("Customer don't exist. Username - " + username);
        }
    }

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
