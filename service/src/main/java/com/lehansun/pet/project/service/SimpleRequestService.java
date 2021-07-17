package com.lehansun.pet.project.service;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.api.dao.LanguageDao;
import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.api.service.RequestService;
import com.lehansun.pet.project.model.Request;
import com.lehansun.pet.project.model.RequestSortType;
import com.lehansun.pet.project.model.dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class SimpleRequestService extends AbstractService<Request> implements RequestService {

    public static final String UNKNOWN_SORTING_TYPE = "Failed to sort requests. Unknown sorting type: ";

    private final RequestDao requestDao;
    private final CustomerDao customerDao;
    private final LanguageDao languageDao;

    public SimpleRequestService(RequestDao requestDao, CustomerDao customerDao, LanguageDao languageDao, ModelMapper modelMapper) {
        super(requestDao, modelMapper);
        this.requestDao = requestDao;
        this.customerDao = customerDao;
        this.languageDao = languageDao;
    }

    @Override
    public List<RequestDTO> getAllDTOs() {
        List<Request> requests = getAll();
        java.lang.reflect.Type targetListType = new TypeToken<List<RequestDTO>>() {}.getType();
        List<RequestDTO> requestDTOs = modelMapper.map(requests, targetListType);
        return requestDTOs;
    }

    @Override
    public RequestDTO getDtoById(long id) {

        Optional<Request> byId = getById(id);
        if (byId.isPresent()) {
            return modelMapper.map(byId.get(), RequestDTO.class);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_ID, id);
            log.error(message);
            throw new RuntimeException(message);
        }
        
    }

    @Override
    public RequestDTO saveByDTO(RequestDTO requestDTO) {
        Request request = modelMapper.map(requestDTO, Request.class);
        save(request);
        requestDTO.setId(request.getId());
        return requestDTO;
    }

    @Override
    public void updateByDTO(long id, RequestDTO requestDTO) {
        Optional<Request> byId = getById(id);
        if (byId.isPresent()) {
            Request request = byId.get();
            prepareToUpdate(requestDTO, request);
            requestDao.update(request);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_ID, id);
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    @Override
    public List<RequestDTO> sort(List<RequestDTO> dtoList, RequestSortType sortType) {
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

    private void prepareToUpdate(RequestDTO requestDTO, Request request) {
        request.setInitiatedBy(customerDao.getById(requestDTO.getInitiatedBy().getId()).get());
        request.setAcceptedBy(customerDao.getById(requestDTO.getAcceptedBy().getId()).get());

        request.setRequestedLanguage(languageDao.getById(requestDTO.getRequestedLanguage().getId()).get());
        request.setWishedStartTime(requestDTO.getWishedStartTime());
        request.setWishedEndTime(requestDTO.getWishedEndTime());
        request.setAcceptedStartTime(requestDTO.getAcceptedStartTime());
    }
}
