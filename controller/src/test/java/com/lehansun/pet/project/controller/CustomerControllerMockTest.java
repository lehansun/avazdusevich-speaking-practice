package com.lehansun.pet.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.controller.config.SpeakingPracticeAppExceptionHandler;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.model.dto.CustomerDtoWithPassword;
import com.lehansun.pet.project.util.EntityGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerMockTest {

    private MockMvc mockMvc;

    private static final String TESTING_ENDPOINT = "/customers";

    private ObjectMapper objectMapper = new ObjectMapper();
    private ModelMapper modelMapper = new ModelMapper();


    @Mock
    private CustomerService customerService;
    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @InjectMocks
    private CustomerController customerController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(new SpeakingPracticeAppExceptionHandler())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }


    @Test
    void getAll_shouldReturnListOfAllCustomerDTOs() throws Exception {
        CustomerDTO firstDTO = EntityGenerator.getNewCustomerDTO();
        CustomerDTO secondDTO = EntityGenerator.getNewCustomerDTO();
        List<CustomerDTO> dtoList = List.of(firstDTO, secondDTO);

        when(customerService.getAllDTOs()).thenReturn(dtoList);

        mockMvc.perform(
                get(TESTING_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(dtoList)));

        verify(customerService, times(1)).getAllDTOs();
        verifyNoMoreInteractions(customerService);
    }

    @Test
    void getById_shouldFindByExistingId() throws Exception {
        long id = 1;
        CustomerDTO customerDTO = EntityGenerator.getNewCustomerDTO();
        customerDTO.setId(id);

        when(customerService.getDtoById(1)).thenReturn(customerDTO);

        mockMvc.perform(
                get(TESTING_ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(customerDTO)));

        verify(customerService, times(1)).getDtoById(id);
    }

    @Test
    void getById_shouldThrowRuntimeException() throws Exception {

        when(customerService.getDtoById(1)).thenThrow(new RuntimeException("Element does not exist"));

        mockMvc.perform(
                get(TESTING_ENDPOINT + "/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(RuntimeException.class));

        verify(customerService, times(1)).getDtoById(1);
    }

    @Test
    void createCustomer_shouldReturnStatus201andCustomerDTO() throws Exception {
        CustomerDtoWithPassword dtoWithPassword = EntityGenerator.getNewCustomerDTOWithPassword();
        CustomerDTO customerDTO = EntityGenerator.getNewCustomerDTO();
        customerDTO.setId(1L);
        when(customerService.saveByDTO(any())).thenReturn(customerDTO);

        mockMvc.perform(
                post(TESTING_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoWithPassword))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(customerDTO)));

        verify(customerService, times(1)).saveByDTO(any());
    }

    @Test
    void updateCustomer_shouldReturnStatus204() throws Exception {
        CustomerDTO customerDTO = EntityGenerator.getNewCustomerDTO();

        mockMvc.perform(
                patch(TESTING_ENDPOINT + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerDTO))
        )
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).updateByDTO(anyLong(), any());
    }

    @Test
    void deleteCustomer_shouldReturnStatus204() throws Exception {

        mockMvc.perform(
                delete(TESTING_ENDPOINT + "/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).delete(anyLong());
    }
}