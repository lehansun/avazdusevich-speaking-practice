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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAll(@RequestParam(value="sortType",
            required=false, defaultValue="BY_START_TIME") String sortType) {
        log.debug("Received Get request: /requests");
        List<RequestDTO> dtoList = requestService.getAllDTOs();
        dtoList = requestService.sort(dtoList, RequestSortType.valueOf(sortType));
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getById(@PathVariable Long id) {
        log.debug("Received Get request: /requests/" + id);
        return ResponseEntity.ok(requestService.getDtoById(id));
    }

    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@RequestBody RequestDTO request) {
        log.debug("Received Post request: /requests");
        return ResponseEntity.status(201).body(requestService.saveByDTO(request));
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<Void> updateRequest(@RequestBody RequestDTO request, @PathVariable("id") long id) {
        log.debug("Received patch request: /requests/" + id);
        requestService.updateByDTO(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("id") long id) {
        log.debug("Received delete request: /requests/" + id);
        requestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}




