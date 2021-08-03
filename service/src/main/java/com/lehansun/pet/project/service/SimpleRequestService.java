package com.lehansun.pet.project.service;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.api.dao.LanguageDao;
import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.api.service.RequestService;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Language;
import com.lehansun.pet.project.model.Request;
import com.lehansun.pet.project.model.RequestSortType;
import com.lehansun.pet.project.model.dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An service class which provides interaction
 * with the Request model.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Service
@Transactional
public class SimpleRequestService extends AbstractService<Request> implements RequestService {

    public static final String UNKNOWN_SORTING_TYPE = "Failed to sort requests. Unknown sorting type: ";
    public static final String REQUEST_ALREADY_ACCEPTED_FORMATTER = "Request-%d already accepted or deleted.";
    public static final String ACCEPTANCE_DENIED = "Acceptance denied.";
    /**
     * A Request data access object.
     */
    private final RequestDao requestDao;

    /**
     * A Customer data access object.
     */
    private final CustomerDao customerDao;

    /**
     * A language data access object.
     */
    private final LanguageDao languageDao;

    /**
     * Constructs new object with given DAOs and ModelMapper object.
     *
     * @param requestDao Request DAO.
     * @param customerDao Customer DAO.
     * @param languageDao Language DAO.
     * @param modelMapper ModelMapper object.
     */
    public SimpleRequestService(RequestDao requestDao, CustomerDao customerDao, LanguageDao languageDao, ModelMapper modelMapper) {
        super(requestDao, modelMapper);
        this.requestDao = requestDao;
        this.customerDao = customerDao;
        this.languageDao = languageDao;
    }

    /**
     * Finds all requests.
     *
     * @return list of request DTOs.
     */
    @Override
    public List<RequestDTO> getAllDTOs() {
        List<Request> requests = getAll();
        java.lang.reflect.Type targetListType = new TypeToken<List<RequestDTO>>() {}.getType();
        return modelMapper.map(requests, targetListType);
    }

    /**
     * Finds request by Id.
     *
     * @param id request Id.
     * @return request DTO.
     */
    @Override
    public RequestDTO getDtoById(long id) {

        log.debug("IN getDtoById({}).", id);
        Optional<Request> byId = getById(id);
        if (byId.isPresent()) {
            return modelMapper.map(byId.get(), RequestDTO.class);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_ID, id);
            log.warn(message);
            throw new RuntimeException(message);
        }
        
    }

    /**
     * Creates and save new request.
     *
     * @param requestDTO request to save.
     * @return request DTO with new assigned ID.
     */
    @Transactional
    @Override
    public RequestDTO saveByDTO(RequestDTO requestDTO) {
        log.debug("IN saveByDTO({}).", requestDTO);
        Request request = new Request();
        prepareToUpdate(requestDTO, request);
        save(request);
        requestDTO.setId(request.getId());
        return requestDTO;
    }

    /**
     * Updates request.
     *
     * @param id id of request to update.
     * @param requestDTO an object containing fields to update.
     */
    @Override
    public void updateByDTO(long id, RequestDTO requestDTO) {
        log.debug("IN updateByDTO({}, {}).", id, requestDTO);
        Optional<Request> byId = getById(id);
        if (byId.isPresent()) {
            Request request = byId.get();
            prepareToUpdate(requestDTO, request);
            requestDao.update(request);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_ID, id);
            log.warn(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Sorts list of requests
     *
     * @param dtoList  list of requests to sort
     * @param sortType list sorting type
     * @return sorted list of request DTOs
     */
    @Override
    public List<RequestDTO> sort(List<RequestDTO> dtoList, RequestSortType sortType) {
        log.debug("IN sort().");
        List<RequestDTO> sortedList = new ArrayList<>(dtoList);
        switch (sortType) {
            case BY_INITIATOR: sortedList.sort(Comparator.comparing(RequestDTO::getInitiatedBy));
                break;
            case BY_ACCEPTOR: sortedList.sort(Comparator.comparing(RequestDTO::getAcceptedBy));
                break;
            case BY_START_TIME: sortedList.sort(Comparator.comparing(RequestDTO::getWishedStartTime));
                break;
            case BY_END_TIME: sortedList.sort(Comparator.comparing(RequestDTO::getWishedEndTime));
                break;
            default:
                log.warn(UNKNOWN_SORTING_TYPE + sortType);
                throw new RuntimeException(UNKNOWN_SORTING_TYPE + sortType);
        }
        return sortedList;
    }

    /**
     * Finds all requests initiated by certain customer for the specified period.
     *
     * @param username username of customer who initiated requests.
     * @param dateFrom period start date.
     * @param dateTo period finish date.
     * @param isAccepted the parameter displays whether the request should be accepted or not.
     * @return list of request DTOs.
     */
    @Override
    public List<RequestDTO> getDTOsInitiatedBy(String username, LocalDate dateFrom, LocalDate dateTo, Boolean isAccepted) {
        log.debug("IN getInitiatedBy({}, {}, {}, {}).", username, dateFrom, dateTo, isAccepted);
        Optional<Customer> optional = customerDao.getByUsername(username);
        if (optional.isPresent()) {
            java.lang.reflect.Type targetListType = new TypeToken<List<RequestDTO>>() {}.getType();
            List<Request> requests = requestDao.getByInitiator(optional.get(), dateFrom, dateTo, isAccepted);
            return modelMapper.map(requests, targetListType);
        }
        String message = String.format(ELEMENT_WITH_NON_EXISTENT_USERNAME, username);
        log.warn(message);
        throw new RuntimeException(message);
    }

    /**
     * Finds all requests initiated by any customer
     * except certain customer specified in the parameters.
     *
     * @param username username of customer to exclude from search.
     * @param dateFrom period start date.
     * @param dateTo period finish date.
     * @param languageName the name of requested language.
     * @return list of request DTOs.
     */
    @Override
    public List<RequestDTO> getOtherCustomersRequestDTOs(String username, LocalDate dateFrom, LocalDate dateTo, String languageName) {
        log.debug("IN getOtherCustomersRequestDTOs({}, {}, {}, {}).", username, dateFrom, dateTo, languageName);
        Optional<Customer> optional = customerDao.getByUsername(username);
        if (optional.isPresent()) {
            Language language = languageName == null ? null : languageDao.getByName(languageName).get();
            java.lang.reflect.Type targetListType = new TypeToken<List<RequestDTO>>() {}.getType();
            List<Request> requests = requestDao.getOtherCustomersRequests(optional.get(), dateFrom, dateTo, language);
            return modelMapper.map(requests, targetListType);
        }
        String message = String.format(ELEMENT_WITH_NON_EXISTENT_USERNAME, username);
        log.warn(message);
        throw new IllegalArgumentException(message);
    }

    /**
     * Finds the request by id and set it accepted
     * by customer with specified username
     *
     * @param requestId id of request to accept
     * @param username username of customer how accept the request
     */
    @Override
    public void attemptToSetAccepted(long requestId, String username) {
        log.debug("IN attemptToSetAccepted(request-{}, {}).", username, requestId);
        Optional<Customer> optionalCustomer = customerDao.getByUsername(username);
        Optional<Request> optionalRequest = getById(requestId);
        if (optionalRequest.isPresent()) {
            Request request = optionalRequest.get();
            if (request.getAcceptedBy() == null) {
                optionalCustomer.ifPresent(customer -> setAccepted(customer, request));
            } else {
                String message = String.format(REQUEST_ALREADY_ACCEPTED_FORMATTER, requestId);
                log.warn(message);
                throw new RuntimeException(message);
            }
        }
    }

    /**
     * Set request accepted by specified customer
     *
     * @param customer customer
     * @param request request
     */
    private void setAccepted(Customer customer, Request request) {
        log.debug("IN setAccepted({}, request-{}).", customer.getUsername(), request.getId());
        if (!Objects.equals(customer.getId(), request.getInitiatedBy().getId())
                && customer.getNativeLanguage().equals(request.getRequestedLanguage())) {
            request.setAcceptedBy(customer);
            update(request);
        } else {
            log.warn(ACCEPTANCE_DENIED);
            throw new IllegalArgumentException(ACCEPTANCE_DENIED);
        }
    }

    /**
     * Prepare customer to update
     *
     * @param requestDTO an object containing fields to update.
     * @param request request to update.
     */
    private void prepareToUpdate(RequestDTO requestDTO, Request request) {
        if (requestDTO.getInitiatedBy() != null) {
            Long id = requestDTO.getInitiatedBy().getId();
            Optional<Customer> optionalCustomer = customerDao.getById(id);
            optionalCustomer.ifPresent(request::setInitiatedBy);
        }
        if (requestDTO.getAcceptedBy() != null) {
            Long id = requestDTO.getAcceptedBy().getId();
            Optional<Customer> optionalCustomer = customerDao.getById(id);
            optionalCustomer.ifPresent(request::setInitiatedBy);
        }
        if (requestDTO.getRequestedLanguage() != null) {
            Long id = requestDTO.getRequestedLanguage().getId();
            Optional<Language> optionalLanguage = languageDao.getById(id);
            optionalLanguage.ifPresent(request::setRequestedLanguage);
        }
        request.setWishedStartTime(requestDTO.getWishedStartTime());
        request.setWishedEndTime(requestDTO.getWishedEndTime());
        request.setAcceptedStartTime(requestDTO.getAcceptedStartTime());
    }
}
