package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.LanguageDao;
import com.lehansun.pet.project.model.Language;
import org.springframework.stereotype.Repository;

/**
 * This class contains methods for direct access to data source using
 * Spring JPA. The object of this class allows to manipulate the
 * models of the Language.class
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Repository
public class LanguageJpaDao extends AbstractJpaDao<Language> implements LanguageDao {

    /**
     * Defines the class of manipulated entity.
     *
     * @return Language.class.
     */
    @Override
    protected Class<Language> getClazz() {
        return Language.class;
    }

}
