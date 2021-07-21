package com.lehansun.pet.project.service;

import com.lehansun.pet.project.api.dao.CountryDao;
import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.api.dao.LanguageDao;
import com.lehansun.pet.project.api.service.CustomerService;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class SimpleCustomerService extends AbstractService<Customer> implements CustomerService {

    private final CustomerDao customerDao;
    private final LanguageDao languageDao;
    private final CountryDao countryDao;

    public SimpleCustomerService(CustomerDao customerDao, LanguageDao languageDao, CountryDao countryDao, ModelMapper modelMapper) {
        super(customerDao, modelMapper);
        this.customerDao = customerDao;
        this.languageDao = languageDao;
        this.countryDao = countryDao;
    }

    @Override
    public List<CustomerDTO> getAllDTOs() {
        List<Customer> customers = getAll();
        java.lang.reflect.Type targetListType = new TypeToken<List<CustomerDTO>>() {}.getType();
        return modelMapper.map(customers, targetListType);
    }

    @Override
    public CustomerDTO getDtoById(long id) {
        Optional<Customer> byId = getById(id);
        if (byId.isPresent()) {
            return modelMapper.map(byId.get(), CustomerDTO.class);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_ID, id);
            log.warn(message);
            throw new RuntimeException(message);
        }
    }

    @Override
    public CustomerDTO getDtoByUsername(String username) {

        Optional<Customer> byUsername = customerDao.getByUsername(username);
        if (byUsername.isPresent()) {
            return modelMapper.map(byUsername.get(), CustomerDTO.class);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_USERNAME, username);
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    @Transactional
    @Override
    public CustomerDTO saveByDTO(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        save(customer);
        customerDTO.setId(customer.getId());
        return customerDTO;
    }

    @Override
    public void updateByDTO(long id, CustomerDTO customerDTO) {
        Optional<Customer> byId = getById(id);
        if (byId.isPresent()) {
            Customer customer = byId.get();
            prepareToUpdate(customerDTO, customer);
            customerDao.update(customer);
        } else {
            String message = String.format(ELEMENT_WITH_NON_EXISTENT_ID, id);
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    private void prepareToUpdate(CustomerDTO customerDTO, Customer customer) {
        customer.setFirstname(customerDTO.getFirstname());
        customer.setLastname(customerDTO.getLastname());
        customer.setEmail(customerDTO.getEmail());
        customer.setLearningLanguage(languageDao.getById(customerDTO.getLearningLanguage().getId()).get());
        customer.setNativeLanguage(languageDao.getById(customerDTO.getNativeLanguage().getId()).get());
        customer.setDateOfBirth(customerDTO.getDateOfBirth());
        customer.setCountry(countryDao.getById(customerDTO.getCountry().getId()).get());
    }
}
