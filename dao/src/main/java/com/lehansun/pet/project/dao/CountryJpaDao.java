package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.CountryDao;
import com.lehansun.pet.project.model.Country;
import org.springframework.stereotype.Repository;

/**
 * This class contains methods for direct access to data source using
 * Spring JPA. The object of this class allows to manipulate the
 * models of the Country.class
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Repository
public class CountryJpaDao extends AbstractJpaDao<Country> implements CountryDao {

    /**
     * Defines the class of dependent entity.
     *
     * @return Country.class.
     */
    @Override
    protected Class<Country> getClazz() {
        return Country.class;
    }
}
