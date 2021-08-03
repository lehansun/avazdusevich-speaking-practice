package com.lehansun.pet.project.util;

import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Language;
import com.lehansun.pet.project.model.Request;
import com.lehansun.pet.project.model.Role;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.model.dto.CustomerDtoWithPassword;
import com.lehansun.pet.project.model.dto.RequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EntityGenerator {


    public static Customer getNewCustomer() {
        log.info("IN getNewCustomer()");
        Customer customer = new Customer();
        customer.setUsername(RandomStringUtils.randomAlphabetic(5, 45));
        customer.setFirstname(RandomStringUtils.randomAlphabetic(5, 45));
        customer.setLastname(RandomStringUtils.randomAlphabetic(5, 45));
        customer.setEmail(RandomStringUtils.randomAlphabetic(5, 45));
        customer.setPassword(RandomStringUtils.randomAlphanumeric(7, 255));
        customer.setNativeLanguage(getNativeLanguage());
        customer.setLearningLanguage(getLearningLanguage());
        customer.setRoles(getRoles());
        return customer;
    }

    public static Request getNewRequest() {
        log.info("IN getNewRequest()");
        Request request = new Request();
        Customer customer = getNewCustomer();
        customer.setId(1L);
        request.setInitiatedBy(customer);
        request.setRequestedLanguage(getNativeLanguage());
        LocalDate now = LocalDate.now();
        request.setWishedStartTime(now.plusDays(1));
        request.setWishedEndTime(now.plusDays(2));
        return request;
    }
    
    public static CustomerDTO getNewCustomerDTO() {
        log.info("IN getNewCustomerDTO()");
        Customer newCustomer = getNewCustomer();
        return new ModelMapper().map(newCustomer, CustomerDTO.class);
    }

    public static RequestDTO getNewRequestDTO() {
        log.info("IN getNewRequestDTO()");
        Request newRequest = getNewRequest();
        return new ModelMapper().map(newRequest, RequestDTO.class);
    }

    private static Language getNativeLanguage() {
        log.info("IN getNativeLanguage()");
        Language language = new Language();
        language.setName("Russian");
        language.setId(1L);
        return language;
    }

    private static Language getLearningLanguage() {
        log.info("IN getLearningLanguage()");
        Language language = new Language();
        language.setName("English");
        language.setId(2L);
        return language;
    }

    private static List<Role> getRoles() {
        log.info("IN getRoles   ()");
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setName("USER");
        role.setId(1L);
        roles.add(role);
        return roles;
    }

    public static CustomerDtoWithPassword getNewCustomerDTOWithPassword() {
        log.info("IN CustomerDtoWithPassword()");
        Customer newCustomer = getNewCustomer();
        return new ModelMapper().map(newCustomer, CustomerDtoWithPassword.class);
    }
}
