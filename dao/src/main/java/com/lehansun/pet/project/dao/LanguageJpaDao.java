package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.LanguageDao;
import com.lehansun.pet.project.model.Language;
import org.springframework.stereotype.Repository;

@Repository
public class LanguageJpaDao extends AbstractJpaDao<Language> implements LanguageDao {

    @Override
    protected Class<Language> getClazz() {
        return Language.class;
    }

}
