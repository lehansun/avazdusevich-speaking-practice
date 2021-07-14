package com.lehansun.pet.project.util;

import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Language;
import com.lehansun.pet.project.model.Request;
import com.lehansun.pet.project.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

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
        request.setInitiatedBy(getNewCustomer());
        request.setRequestedLanguage(getNativeLanguage());
        LocalDate now = LocalDate.now();
        request.setWishedStartTime(now.plusDays(1));
        request.setWishedEndTime(now.plusDays(2));
        return request;
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

}
