package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.LanguageDao;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Language;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * This class contains methods for direct access to data source using
 * Spring JPA. The object of this class allows to manipulate the
 * models of the Language.class
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Repository
public class LanguageJpaDao extends AbstractJpaDao<Language> implements LanguageDao {

    private static final String GET_BY_NAME_LOG_MESSAGE = "IN getByName(), language - {}";

    /**
     * Defines the class of manipulated entity.
     *
     * @return Language.class.
     */
    @Override
    protected Class<Language> getClazz() {
        return Language.class;
    }

    /**
     * Finds language by name.
     *
     * @param language language name.
     * @return language.
     */
    @Override
    public Optional<Language> getByName(String language) {
        log.debug(GET_BY_NAME_LOG_MESSAGE, language);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Language> criteriaQuery = criteriaBuilder.createQuery(Language.class);
        Root<Language> root = criteriaQuery.from(Language.class);
        CriteriaQuery<Language> query = criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), language));
        List<Language> resultList = entityManager.createQuery(criteriaQuery).getResultList();
        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }
}
