package com.lehansun.pet.project.api.service;

import com.lehansun.pet.project.model.Language;
import com.lehansun.pet.project.model.Request;
import com.lehansun.pet.project.model.RequestSortType;
import com.lehansun.pet.project.model.dto.RequestDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * A service interface that defines the methods
 * of working with the Request model.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
public interface RequestService extends GenericService<Request> {

    /**
     * Finds all requests.
     *
     * @return list of requestDTOs.
     */
    List<RequestDTO> getAllDTOs();

    /**
     * Finds request by Id.
     *
     * @param id request Id.
     * @return requestDTO.
     */
    RequestDTO getDtoById(long id);

    /**
     * Creates and save new request.
     *
     * @param requestDTO request to save.
     * @return request DTO with new assigned ID.
     */
    RequestDTO saveByDTO(RequestDTO requestDTO);

    /**
     * Updates request.
     *
     * @param id id of request to update.
     * @param requestDTO an object containing fields to update.
     */
    void updateByDTO(long id, RequestDTO requestDTO);

    /**
     * Sorts list of requests
     *
     * @param dtoList list of requests to sort
     * @param sortType list sorting type
     * @return sorted list of request DTOs
     */
    List<RequestDTO> sort(List<RequestDTO> dtoList, RequestSortType sortType);

    /**
     * Finds all requests initiated by certain customer for the specified period.
     *
     * @param username username of customer who initiated requests.
     * @param dateFrom period start date.
     * @param dateTo period finish date.
     * @param isAccepted the parameter displays whether the request should be accepted or not.
     * @return list of request DTOs.
     */
    List<RequestDTO> getDTOsInitiatedBy(String username, LocalDate dateFrom, LocalDate dateTo, Boolean isAccepted);


    /**
     * Finds all requests initiated by any customer
     * except certain customer specified in the parameters.
     *
     * @param username username of customer to exclude from search.
     * @param dateFrom period start date.
     * @param dateTo period finish date.
     * @param language the name of requested language.
     * @return list of request DTOs.
     */
    List<RequestDTO> getOtherCustomersRequestDTOs(String username, LocalDate dateFrom, LocalDate dateTo, String language);
}
