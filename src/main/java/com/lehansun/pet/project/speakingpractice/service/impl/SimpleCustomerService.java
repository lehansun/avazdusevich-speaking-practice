package com.lehansun.pet.project.speakingpractice.service.impl;

import com.lehansun.pet.project.speakingpractice.model.Country;
import com.lehansun.pet.project.speakingpractice.model.Customer;
import com.lehansun.pet.project.speakingpractice.model.Language;
import com.lehansun.pet.project.speakingpractice.model.dto.CustomerDTO;
import com.lehansun.pet.project.speakingpractice.model.dto.CustomerDtoWithPassword;
import com.lehansun.pet.project.speakingpractice.repository.CountryRepository;
import com.lehansun.pet.project.speakingpractice.repository.CustomerRepository;
import com.lehansun.pet.project.speakingpractice.repository.LanguageRepository;
import com.lehansun.pet.project.speakingpractice.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * A service class which provides interaction
 * with the Customer model.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Service
@Transactional
public class SimpleCustomerService extends AbstractService<Customer> implements CustomerService {

    /**
     * A Customer data access object.
     */
    private final CustomerRepository customerRepository;

    /**
     * A Language data access object.
     */
    private final LanguageRepository languageRepository;

    /**
     * A Country data access object.
     */
    private final CountryRepository countryRepository;

    /**
     * Constructs new object with given repositories and ModelMapper object.
     *
     * @param customerRepository Customer repository.
     * @param languageRepository Language repository.
     * @param countryRepository Country repository.
     * @param modelMapper ModelMapper object.
     */
    public SimpleCustomerService(CustomerRepository customerRepository, LanguageRepository languageRepository, CountryRepository countryRepository, ModelMapper modelMapper) {
        super(customerRepository, modelMapper);
        this.customerRepository = customerRepository;
        this.languageRepository = languageRepository;
        this.countryRepository = countryRepository;
    }

    /**
     * Finds all customers.
     *
     * @return list of customer DTOs.
     */
    @Override
    public List<CustomerDTO> getAllDTOs() {
        log.debug("IN getAllDTOs()");
        List<Customer> customers = getAll();
        java.lang.reflect.Type targetListType = new TypeToken<List<CustomerDTO>>() {}.getType();
        return modelMapper.map(customers, targetListType);
    }

    /**
     * Finds customer by ID.
     *
     * @param id customer ID.
     * @return customer DTO.
     */
    @Override
    public CustomerDTO getDtoById(long id) {
        log.debug("IN getDtoById({})", id);
        Optional<Customer> byId = getById(id);
        if (byId.isPresent()) {
            return modelMapper.map(byId.get(), CustomerDTO.class);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_ID, id);
            log.warn(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Finds customer by username.
     *
     * @param username customer username.
     * @return customer DTO.
     */
    @Override
    public CustomerDTO getDtoByUsername(String username) {
        log.debug("IN getDtoByUsername({})", username);
        Optional<Customer> byUsername = customerRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            log.debug("IN getDtoByUsername({}). Customer successfully found.", username);
            return modelMapper.map(byUsername.get(), CustomerDTO.class);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_USERNAME, username);
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Creates and save new customer.
     *
     * @param customerDTO customer to save.
     * @return customer DTO with new assigned ID.
     */
    @Transactional
    @Override
    public CustomerDTO saveByDTO(CustomerDtoWithPassword customerDTO) {
        log.debug("IN saveByDTO({})", customerDTO);
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        save(customer);
        customerDTO.setId(customer.getId());
        return modelMapper.map(customerDTO, CustomerDTO.class);
    }

    /**
     * Updates customer.
     *
     * @param id ID of customer to update
     * @param customerDTO an object containing fields to update.
     */
    @Override
    public void updateByDTO(long id, CustomerDTO customerDTO) {
        log.debug("IN updateByDTO({}, {})", id, customerDTO);
        Optional<Customer> byId = getById(id);
        if (byId.isPresent()) {
            Customer customer = byId.get();
            prepareToUpdate(customerDTO, customer);
            save(customer);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_ID, id);
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    /**
     * Finds customer by username and updates his password.
     *
     * @param username customer's username.
     * @param newPassword new password.
     */
    public void updatePassword(String username, String newPassword) {
        log.debug("IN updatePassword({}): ", username);
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(username);
        optionalCustomer.ifPresent(c -> {
            c.setPassword(newPassword);
            save(optionalCustomer.get());
        });
    }

    /**
     * Prepare customer to update
     *
     * @param customerDTO an object containing fields to update.
     * @param customer customer to update.
     */
    private void prepareToUpdate(CustomerDTO customerDTO, Customer customer) {
        log.debug("IN prepareToUpdate()");
        customer.setUsername(customerDTO.getUsername());
        customer.setFirstname(customerDTO.getFirstname());
        customer.setLastname(customerDTO.getLastname());
        customer.setEmail(customerDTO.getEmail());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        if (customerDTO.getLearningLanguage() != null) {
            Long id = customerDTO.getLearningLanguage().getId();
            Optional<Language> optionalLanguage = languageRepository.findById(id);
            optionalLanguage.ifPresent(customer::setLearningLanguage);
        }
        if (customerDTO.getNativeLanguage() != null) {
            Long id = customerDTO.getNativeLanguage().getId();
            Optional<Language> optionalLanguage = languageRepository.findById(id);
            optionalLanguage.ifPresent(customer::setNativeLanguage);
        }
        if (customerDTO.getCountry() != null) {
            Long id = customerDTO.getCountry().getId();
            Optional<Country> optionalCountry = countryRepository.findById(id);
            optionalCountry.ifPresent(customer::setCountry);
        }
    }

}
