package com.lehansun.pet.project.speakingpractice.repository;

import com.lehansun.pet.project.speakingpractice.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A simple  Repository interface to handle the database operations
 * required to manipulate the Language model.
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    /**
     * Finds language by name.
     *
     * @param language language name.
     * @return language.
     */
    Optional<Language> findByName(String language);
}
