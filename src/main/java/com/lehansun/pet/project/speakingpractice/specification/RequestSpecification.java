package com.lehansun.pet.project.speakingpractice.specification;

import com.lehansun.pet.project.speakingpractice.model.Customer;
import com.lehansun.pet.project.speakingpractice.model.Language;
import com.lehansun.pet.project.speakingpractice.model.Request;
import com.lehansun.pet.project.speakingpractice.model.Request_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.data.jpa.domain.Specification.where;

/**
 * ToDo
 */
public class RequestSpecification {

    /**
     * ToDo
     *
     * @param customer
     * @param dateFrom
     * @param dateTo
     * @param isAccepted
     * @return
     */
    public static Specification<Request> filterPersonalRequests(Customer customer, LocalDate dateFrom, LocalDate dateTo, Boolean isAccepted) {
        return (root, query, builder) -> where(getPeriodPredicate(dateFrom, dateTo))
                .and(getPredicateForPersonalRequests(customer))
                .and(getPredicateByConfirmationStatus(isAccepted))
                .toPredicate(root, query, builder);
    }

    /**
     * ToDo
     *
     * @param customer
     * @param dateFrom
     * @param dateTo
     * @param language
     * @return
     */
    public static Specification<Request> filterNotPersonalRequests(Customer customer, LocalDate dateFrom, LocalDate dateTo, Language language) {
        return (root, query, builder) -> where(getPeriodPredicate(dateFrom, dateTo))
                .and(getPredicateForNotPersonalRequests(customer))
                .and(getLanguagePredicate(language))
                .toPredicate(root, query, builder);
    }

    /**
     * ToDo
     *
     * @param dateFrom
     * @param dateTo
     * @return
     */
    private static Specification<Request> getPeriodPredicate(final LocalDate dateFrom, final LocalDate dateTo) {
        if (dateFrom == null && dateTo == null)
            return null;

        if (dateFrom == null)
            return (root, query, builder) -> builder.lessThanOrEqualTo(root.get(Request_.WISHED_START_TIME), dateTo);

        if (dateTo == null)
            return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get(Request_.WISHED_START_TIME), dateFrom);

        return (root, query, builder) -> builder.between(root.get(Request_.WISHED_START_TIME), dateFrom, dateTo);
    }

    /**
     * ToDo
     *
     * @param customer
     * @return
     */
    private static Specification<Request> getPredicateForPersonalRequests(Customer customer) {
        if (Objects.isNull(customer)) {
            return null;
        }

        return (root, query, builder) -> builder.equal(root.get(Request_.INITIATED_BY), customer);
    }

    /**
     * ToDo
     *
     * @param customer
     * @return
     */
    private static Specification<Request> getPredicateForNotPersonalRequests(Customer customer) {
        if (Objects.isNull(customer)) {
            return null;
        }

        return (root, query, builder) -> builder.notEqual(root.get(Request_.INITIATED_BY), customer);
    }

    /**
     * ToDo
     *
     * @param confirmationStatus
     * @return
     */
    private static Specification<Request> getPredicateByConfirmationStatus(Boolean confirmationStatus) {
        if (Objects.isNull(confirmationStatus)) {
            return null;
        }

        if (confirmationStatus) {
            return (root, query, builder) -> builder.isNotNull(root.get(Request_.ACCEPTED_BY));
        }

        return (root, query, builder) -> builder.isNull(root.get(Request_.ACCEPTED_BY));
    }

    /**
     * ToDo
     *
     * @param language
     * @return
     */
    private static Specification<Request> getLanguagePredicate(Language language) {
        if (Objects.isNull(language)) {
            return null;
        }

        return (root, query, builder) -> builder.equal(root.get(Request_.REQUESTED_LANGUAGE), language);
    }
}
