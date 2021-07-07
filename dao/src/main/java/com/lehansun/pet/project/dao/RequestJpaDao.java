package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.model.Request;
import org.springframework.stereotype.Repository;

/**
 * This class contains methods for direct access to data source using
 * Spring JPA. The object of this class allows to manipulate the
 * models of the Request.class
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Repository
public class RequestJpaDao extends AbstractJpaDao<Request> implements RequestDao {

    /**
     * Defines the class of manipulated entity.
     *
     * @return dependent entity class.
     */
    @Override
    protected Class<Request> getClazz() {
        return Request.class;
    }

}
