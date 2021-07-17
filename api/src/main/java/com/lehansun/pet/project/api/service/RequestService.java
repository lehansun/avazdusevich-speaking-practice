package com.lehansun.pet.project.api.service;

import com.lehansun.pet.project.model.Request;
import com.lehansun.pet.project.model.RequestSortType;
import com.lehansun.pet.project.model.dto.RequestDTO;

import java.util.List;

public interface RequestService extends GenericService<Request> {

    List<RequestDTO> getAllDTOs();
    RequestDTO getDtoById(long id);
    RequestDTO saveByDTO(RequestDTO request);
    void updateByDTO(long id, RequestDTO requestDTO);
    List<RequestDTO> sort(List<RequestDTO> dtoList, RequestSortType sortType);
}
