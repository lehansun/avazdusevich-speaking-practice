package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains methods for direct access to data source using
 * Spring JPA. The object of this class allows to manipulate the
 * models of the Request.class
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Repository
public class RequestJpaDao extends AbstractJpaDao<Request> implements RequestDao {


    /**
     * Finds all requests initiated by certain customer for the specified period.
     *
     * @param customer customer who initiated requests.
     * @param dateFrom period start date.
     * @param dateTo period end date.
     * @param isAccepted the parameter displays whether the request should be accepted or not.
     * @return list of requests.
     */
    @Override
    public List<Request> getByInitiator(Customer customer, LocalDate dateFrom, LocalDate dateTo, Boolean isAccepted) {
        log.debug("IN getByInitiator({}, {}, {}, {}).",customer.getUsername(), dateFrom, dateTo, isAccepted);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> request = criteriaQuery.from(Request.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(request.get("initiatedBy"), customer));
        addPeriodPredicates(dateFrom, dateTo, criteriaBuilder, request, predicates);
        addAcceptorPredicate(isAccepted, criteriaBuilder, request, predicates);
        CriteriaQuery<Request> query = criteriaQuery.select(request).where(predicates.toArray(new Predicate[]{}));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    /**
     * Defines the class of manipulated entity.
     *
     * @return Request.class.
     */
    @Override
    protected Class<Request> getClazz() {
        return Request.class;
    }

    /**
     * Adds predicates to the list of predicates if the period is specified.
     *
     * @param dateFrom start of period.
     * @param dateTo end of period.
     * @param criteriaBuilder object used to construct predicates.
     * @param request request metamodel.
     * @param predicates list of predicates.
     */
    private void addPeriodPredicates(LocalDate dateFrom, LocalDate dateTo, CriteriaBuilder criteriaBuilder, Root<Request> request, List<Predicate> predicates) {
        if (dateFrom != null && dateTo == null) {
            log.debug("Start of period is specified: {}.", dateFrom);
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(request.get("wishedStartTime"), dateFrom));
        }
        if (dateFrom == null && dateTo != null) {
            log.debug("End of period is specified: {}.", dateTo);
            predicates.add(criteriaBuilder.lessThan(request.get("wishedStartTime"), dateTo));
        }
        if (dateFrom != null && dateTo != null) {
            log.debug("Period is specified: from {} to {}.", dateFrom, dateTo);
            if (dateFrom.isBefore(dateTo)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(request.get("wishedStartTime"), dateFrom));
                predicates.add(criteriaBuilder.lessThan(request.get("wishedStartTime"), dateTo));
            }else {
                log.warn("'Date from' cannot be after 'date to'.");
                throw new IllegalArgumentException("'Date from' cannot be after 'date to'.");
            }
        }
    }

    /**
     * Adds predicates to the list of predicates if the acceptor status specified.
     *
     * @param isAccepted acceptor status
     * @param criteriaBuilder object used to construct predicates.
     * @param request request metamodel.
     * @param predicates list of predicates.
     */
    private void addAcceptorPredicate(Boolean isAccepted, CriteriaBuilder criteriaBuilder, Root<Request> request, List<Predicate> predicates) {
        if (isAccepted != null) {
            if (!isAccepted) {
                log.debug("Specified status 'Non accepted'.");
                predicates.add(criteriaBuilder.isNull(request.get("acceptedBy")));
            } else {
                log.debug("Specified status 'Accepted'.");
                predicates.add(criteriaBuilder.isNotNull(request.get("acceptedBy")));

            }
        }
    }

}
