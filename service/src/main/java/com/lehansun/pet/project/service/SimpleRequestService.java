package com.lehansun.pet.project.service;

import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.api.service.RequestService;
import com.lehansun.pet.project.model.Request;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SimpleRequestService extends AbstractService<Request> implements RequestService {

    private final RequestDao requestDao;

    public SimpleRequestService(RequestDao requestDao, ModelMapper modelMapper) {
        super(requestDao, modelMapper);
        this.requestDao = requestDao;
    }
}
