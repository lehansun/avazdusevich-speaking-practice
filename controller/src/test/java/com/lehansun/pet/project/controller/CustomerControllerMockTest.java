package com.lehansun.pet.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.model.dto.CustomerDTO;
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

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CustomerControllerMockTest {

    private MockMvc mockMvc;

    private static final String CUSTOMERS_ENDPOINT = "/customers";

    ObjectMapper objectMapper = new ObjectMapper();
    ModelMapper modelMapper = new ModelMapper();

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }


    @Test
    void getAll_shouldReturnListOfAllCustomerDTOs() throws Exception {
        CustomerDTO firstDTO = modelMapper.map(EntityGenerator.getNewCustomer(), CustomerDTO.class);
        CustomerDTO secondDTO = modelMapper.map(EntityGenerator.getNewCustomer(), CustomerDTO.class);
        List<CustomerDTO> dtoList = List.of(firstDTO, secondDTO);

        when(customerService.getAllDTOs()).thenReturn(dtoList);

        mockMvc.perform(get(CUSTOMERS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(objectMapper.writeValueAsString(dtoList)));

        verify(customerService, times(1)).getAllDTOs();
        verifyNoMoreInteractions(customerService);
    }

    void getById() {
    }

    void createCustomer() {
    }

    void updateCustomer() {
    }

    void deleteCustomer() {
    }
}