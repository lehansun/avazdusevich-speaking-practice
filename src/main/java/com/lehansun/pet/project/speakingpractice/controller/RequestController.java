package com.lehansun.pet.project.speakingpractice.controller;

import com.lehansun.pet.project.speakingpractice.model.dto.RequestDTO;
import com.lehansun.pet.project.speakingpractice.model.enumiration.RequestSortType;
import com.lehansun.pet.project.speakingpractice.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/request")
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
        log.debug("Received Get request: /request");
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
        log.debug("Received Get request: /request/" + id);
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
        log.debug("Received Post request: /request");
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
        log.debug("Received patch request: /request/" + id);
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
        log.debug("Received delete request: /request/" + id);
        requestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
