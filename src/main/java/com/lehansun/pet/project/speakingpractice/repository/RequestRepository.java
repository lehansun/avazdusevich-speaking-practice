package com.lehansun.pet.project.speakingpractice.repository;

import com.lehansun.pet.project.speakingpractice.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * A simple  Repository interface to handle the database operations
 * required to manipulate the Request model.
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {
}
