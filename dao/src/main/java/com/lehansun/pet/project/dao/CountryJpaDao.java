package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.CountryDao;
import com.lehansun.pet.project.model.Country;
import org.springframework.stereotype.Repository;

@Repository
public class CountryJpaDao extends AbstractJpaDao<Country> implements CountryDao {
    @Override
    protected Class<Country> getClazz() {
        return Country.class;
    }
}
