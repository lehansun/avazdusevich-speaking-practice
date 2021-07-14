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
        List<CustomerDTO> customerDTOs = modelMapper.map(customers, targetListType);
        return customerDTOs;
    }

    @Override
    public CustomerDTO getDtoById(long id) {
        Optional<Customer> byId = getById(id);
        if (byId.isPresent()) {
            return modelMapper.map(byId.get(), CustomerDTO.class);
        } else {
            String message = String.format(ELEMENT_DOES_NOT_EXIST, id);
            log.error(message);
            throw new RuntimeException(message);
        }
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDTO) {
        Customer customer = modelMapper.map(customerDTO, Customer.class);
        save(customer);
        customerDTO.setId(customer.getId());
        return customerDTO;
    }

    @Override
    public CustomerDTO getByUsername(String username) {
        Customer customer = customerDao.getByUsername(username);
        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Override
    public void update(long id, CustomerDTO customerDTO) {

    }
}
