package com.lehansun.pet.project.controller;

import com.lehansun.pet.project.api.service.RequestService;
import com.lehansun.pet.project.model.RequestSortType;
import com.lehansun.pet.project.model.dto.RequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest controller for work with Requests.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    /**
     * Service layer object to get information about Requests.
     */
    private final RequestService requestService;

    /**
     * Finds all requests and sort them.
     * By default, the list of requests would be sorted in ascending order of start time.
     *
     * @param sortType list sorting type.
     * @return ResponseEntity which contains a sorted List of all found request DTOs.
     */
    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAll(@RequestParam(value="sortType",
            required=false, defaultValue="BY_START_TIME") String sortType) {
        log.debug("Received Get request: /requests");
        List<RequestDTO> dtoList = requestService.getAllDTOs();
        dtoList = requestService.sort(dtoList, RequestSortType.valueOf(sortType));
        return ResponseEntity.ok(dtoList);
    }

    /**
     * Finds request with given ID.
     *
     * @param id request ID.
     * @return ResponseEntity which contains a looked for request DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getById(@PathVariable Long id) {
        log.debug("Received Get request: /requests/" + id);
        return ResponseEntity.ok(requestService.getDtoById(id));
    }

    /**
     * Creates new request.
     *
     * @param requestDTO request.
     * @return ResponseEntity which contains a request DTO with new assigned ID.
     */
    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@RequestBody RequestDTO requestDTO) {
        log.debug("Received Post request: /requests");
        return ResponseEntity.status(201).body(requestService.saveByDTO(requestDTO));
    }

    /**
     * Updates an existing request.
     *
     * @param requestDTO an object containing fields to update.
     * @param id Id of request to update
     * @return ResponseEntity with status 204.
     */
    @PatchMapping("/{id}")
    public  ResponseEntity<Void> updateRequest(@RequestBody RequestDTO requestDTO, @PathVariable("id") long id) {
        log.debug("Received patch request: /requests/" + id);
        requestService.updateByDTO(id, requestDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes request from data source.
     *
     * @param id Id of request to delete.
     * @return ResponseEntity with status 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("id") long id) {
        log.debug("Received delete request: /requests/" + id);
        requestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}




