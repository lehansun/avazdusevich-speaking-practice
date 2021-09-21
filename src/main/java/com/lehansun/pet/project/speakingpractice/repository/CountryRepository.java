package com.lehansun.pet.project.speakingpractice.repository;

import com.lehansun.pet.project.speakingpractice.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A simple  Repository interface to handle the database operations
 * required to manipulate the Country model.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
}
