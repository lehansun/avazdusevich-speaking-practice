package com.lehansun.pet.project.service;

import com.lehansun.pet.project.api.dao.CustomerDao;
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

    public SimpleCustomerService(CustomerDao customerDao, ModelMapper modelMapper) {
        super(customerDao, modelMapper);
        this.customerDao = customerDao;
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
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    @Transactional
    @Override
    public CustomerDTO saveDto(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        save(customer);
        customerDTO.setId(customer.getId());
        return customerDTO;
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

    @Override
    public void updateDto(long id, CustomerDTO customerDTO) {

    }
}
