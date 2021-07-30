package com.lehansun.pet.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.api.service.RequestService;
import com.lehansun.pet.project.security.JwtPasswordValidator;
import com.lehansun.pet.project.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
class PersonalControllerMockTest {

    private MockMvc mockMvc;

    private static final String TESTING_ENDPOINT = "/me";

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CustomerService customerService;
    @Mock
    private RequestService requestService;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private JwtPasswordValidator passwordValidator;

    @InjectMocks
    private PersonalController personalController;

    @Test
    void get() {
    }

    @Test
    void getRequests() {
    }

    @Test
    void changePassword() {
    }
}