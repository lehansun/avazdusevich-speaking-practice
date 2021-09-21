package com.lehansun.pet.project.speakingpractice.repository;

import com.lehansun.pet.project.speakingpractice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A simple  Repository interface to handle the database operations
 * required to manipulate the Customer model.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    /**
     * Finds customer by username.
     *
     * @param username customer's username.
     * @return customer.
     */
    Optional<Customer> findByUsername(String username);

}
