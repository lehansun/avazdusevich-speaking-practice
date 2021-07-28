package com.lehansun.pet.project.service;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.api.dao.LanguageDao;
import com.lehansun.pet.project.api.dao.RequestDao;
import com.lehansun.pet.project.model.Request;
import com.lehansun.pet.project.model.dto.RequestDTO;
import com.lehansun.pet.project.util.EntityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleRequestServiceMockTest {

    @Spy
    private ModelMapper modelMapper;
    @Mock
    private RequestDao mockDao;
    @Mock
    private CustomerDao customerDao;
    @Mock
    private LanguageDao languageDao;
    @InjectMocks
    private SimpleRequestService testingService;

    @Test
    void getAll() {
        // given
        Request request = EntityGenerator.getNewRequest();
        when(mockDao.getAll()).thenReturn(List.of(request));

        // when
        List<Request> requestList = testingService.getAll();

        //then
        assertEquals(1, requestList.size());
        assertEquals(request.getInitiatedBy(), requestList.get(0).getInitiatedBy());
        verify(mockDao, times(1)).getAll();
    }

    @Test
    void getById_shouldFindExistingId() {
        // given
        long id = 1L;
        Request request = EntityGenerator.getNewRequest();
        request.setId(id);
        when(mockDao.getById(id)).thenReturn(Optional.of(request));

        // when
        Optional<Request> byId = testingService.getById(id);

        //then
        assertTrue(byId.isPresent());
        assertEquals(byId.get().getId(), id);
        verify(mockDao, times(1)).getById(any());
    }

    @Test
    void getById_shouldFailOnFindingWithNonExistentId() {
        // given
        long id = -1L;
        String message = "Element with id:-1 does not exist";

        // when
        when(mockDao.getById(anyLong())).thenReturn(Optional.empty());

        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> testingService.getDtoById(id));
        assertEquals(message, runtimeException.getLocalizedMessage());
        verify(mockDao, times(1)).getById(any());
    }

    @Test
    void save_shouldCallDaoSave() {
        // given
        Request request = EntityGenerator.getNewRequest();

        // when
        testingService.save(request);

        //then
        verify(mockDao, times(1)).save(request);
    }

    @Test
    void update_shouldCallDaoUpdate() {
        // given
        Request request = EntityGenerator.getNewRequest();

        // when
        testingService.update(request);

        //then
        verify(mockDao, times(1)).update(request);
    }

    @Test
    void delete_shouldCallDaoDelete() {
        // given
        Request request = EntityGenerator.getNewRequest();

        // when
        testingService.delete(request);

        //then
        verify(mockDao, times(1)).delete(request);
    }

    @Test
    void delete_shouldDeleteById() {
        // given
        Long id = 1L;
        Request request = EntityGenerator.getNewRequest();

        // when
        when(mockDao.getById(id)).thenReturn(Optional.of(request));
        testingService.delete(id);

        //then
        verify(mockDao, times(1)).getById(id);
        verify(mockDao, times(1)).delete(request);
    }

    @Test
    void testDelete_shouldFailOnDeletingByNonExistentId() {
        // given
        Long id = 1L;
        String message= "Failed to delete element id-" + id;

        // when
        when(mockDao.getById(id)).thenReturn(Optional.empty());

        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> testingService.delete(id));
        assertEquals(message, runtimeException.getLocalizedMessage());
        verify(mockDao, times(1)).getById(id);
        verifyNoMoreInteractions(mockDao);
    }

    @Test
    void getAllDTOs() {
        // given
        Request request = EntityGenerator.getNewRequest();
        when(mockDao.getAll()).thenReturn(List.of(request));

        // when
        List<RequestDTO> allDTOs = testingService.getAllDTOs();

        //then
        assertEquals(1, allDTOs.size());
        assertEquals(request.getWishedStartTime(), allDTOs.get(0).getWishedStartTime());
        verify(mockDao, times(1)).getAll();
    }

    @Test
    void getDtoById_shouldFindExistingId() {
        // given
        long id = 1L;
        Request request = EntityGenerator.getNewRequest();
        request.setId(id);
        when(mockDao.getById(id)).thenReturn(Optional.of(request));

        // when
        RequestDTO dto = testingService.getDtoById(id);

        //then
        assertEquals(dto.getId(), id);
        verify(mockDao, times(1)).getById(any());
    }

    @Test()
    void getDtoById_shouldFailOnFindingWithNonExistentId() {
        // given
        long id = -1L;
        String message = "Element with id:-1 does not exist";

        // when
        when(mockDao.getById(id)).thenReturn(Optional.empty());

        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> testingService.getDtoById(id));
        assertEquals(message, runtimeException.getLocalizedMessage());
        verify(mockDao, times(1)).getById(any());
        verifyNoMoreInteractions(mockDao);
    }

    @Test
    void saveByDTO_shouldCallDaoSave() {
        // given
        Long id = 1L;
        RequestDTO requestDTO = EntityGenerator.getNewRequestDTO();
        requestDTO.setId(id);

        // when
        RequestDTO saveByDTO = testingService.saveByDTO(requestDTO);

        // then
        assertEquals(id, saveByDTO.getId());
        verify(mockDao, times(1)).save(any());
    }

    @Test
    void updateByDTO_shouldUpdateByExistingId() {
        // given
        Long id = 1L;
        RequestDTO requestDTO = EntityGenerator.getNewRequestDTO();
        Request request = EntityGenerator.getNewRequest();
        request.setId(id);

        // when
        when(mockDao.getById(id)).thenReturn(Optional.of(request));
        when(customerDao.getById(anyLong())).thenReturn(Optional.empty());
        testingService.updateByDTO(id, requestDTO);

        // then
        assertEquals(requestDTO.getWishedStartTime(), request.getWishedStartTime());
        assertEquals(requestDTO.getWishedEndTime(), request.getWishedEndTime());
        verify(mockDao, times(1)).update(request);
        verify(customerDao, times(1)).getById(anyLong());
        verify(languageDao, times(1)).getById(anyLong());
    }

    @Test
    void updateByDTO_shouldFailOnByNonExistingId() {
        // given
        long id = 1L;
        RequestDTO requestDTO = EntityGenerator.getNewRequestDTO();
        String message = "Element with id:1 does not exist";

        // when
        when(mockDao.getById(anyLong())).thenReturn(Optional.empty());

        // then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> testingService.updateByDTO(id, requestDTO));
        assertEquals(message, runtimeException.getLocalizedMessage());
        verify(mockDao, times(1)).getById(any());
        verifyNoMoreInteractions(mockDao);
    }

}