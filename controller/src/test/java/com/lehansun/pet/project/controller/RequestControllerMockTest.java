package com.lehansun.pet.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lehansun.pet.project.api.service.RequestService;
import com.lehansun.pet.project.controller.config.SpeakingPracticeAppExceptionHandler;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.model.dto.RequestDTO;
import com.lehansun.pet.project.util.EntityGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class RequestControllerMockTest {

    private MockMvc mockMvc;

    private static final String TESTING_ENDPOINT = "/requests";

    private ModelMapper modelMapper = new ModelMapper();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RequestService requestService;

    @InjectMocks
    private RequestController requestController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(requestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(new SpeakingPracticeAppExceptionHandler())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAll_shouldReturnListOfAllRequestDTOsAndStatus200() throws Exception {
        RequestDTO firstDTO = EntityGenerator.getNewRequestDTO();
        RequestDTO secondDTO = EntityGenerator.getNewRequestDTO();
        List<RequestDTO> dtoList = List.of(firstDTO, secondDTO);

        when(requestService.getAllDTOs()).thenReturn(dtoList);
        when(requestService.sort(any(), any())).thenReturn(dtoList);

        mockMvc.perform(
                get(TESTING_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(dtoList)));

        verify(requestService, times(1)).getAllDTOs();
        verify(requestService, times(1)).sort(any(), any());
        verifyNoMoreInteractions(requestService);
    }

    @Test
    void getById_shouldFindByExistingId() throws Exception {
        long id = 1;
        RequestDTO requestDTO = EntityGenerator.getNewRequestDTO();
        requestDTO.setId(id);

        when(requestService.getDtoById(1)).thenReturn(requestDTO);

        mockMvc.perform(
                get(TESTING_ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(requestDTO)));

        verify(requestService, times(1)).getDtoById(id);
    }

    @Test
    void getById_shouldThrowRuntimeExceptionAndStatus400() throws Exception {

        when(requestService.getDtoById(anyLong())).thenThrow(new RuntimeException("Element does not exist"));

        mockMvc.perform(
                get(TESTING_ENDPOINT + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(RuntimeException.class));

        verify(requestService, times(1)).getDtoById(anyLong());
    }

    @Test
    void createRequest_shouldReturnStatus201andRequestDTO() throws Exception {
        RequestDTO requestDTO = EntityGenerator.getNewRequestDTO();
        requestDTO.setId(1L);
        when(requestService.saveByDTO(any())).thenReturn(requestDTO);

        mockMvc.perform(
                post(TESTING_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(requestDTO)));

        verify(requestService, times(1)).saveByDTO(any());
    }

    @Test
    void updateRequest_shouldReturnStatus204() throws Exception {
        RequestDTO requestDTO = EntityGenerator.getNewRequestDTO();

        mockMvc.perform(
                patch(TESTING_ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
        )
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).updateByDTO(anyLong(), any());
    }

    @Test
    void deleteRequest_shouldReturnStatus204() throws Exception {

        mockMvc.perform(
                delete(TESTING_ENDPOINT + "/1"))
                .andExpect(status().isNoContent());

        verify(requestService, times(1)).delete(anyLong());
    }
}