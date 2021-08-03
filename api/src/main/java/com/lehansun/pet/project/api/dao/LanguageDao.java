package com.lehansun.pet.project.api.dao;

import com.lehansun.pet.project.model.Language;

import java.util.Optional;

/**
 * A simple  DAO interface to handle the database operations
 * required to manipulate the Language model.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
public interface LanguageDao extends GenericDao<Language> {

    /**
     * Finds language by name.
     *
     * @param language language name.
     * @return language.
     */
    Optional<Language> getByName(String language);
}
