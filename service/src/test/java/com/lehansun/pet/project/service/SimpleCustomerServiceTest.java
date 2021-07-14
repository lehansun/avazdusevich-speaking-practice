package com.lehansun.pet.project.service;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.dto.CustomerDTO;
import com.lehansun.pet.project.util.EntityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimpleCustomerServiceTest {

    @Spy
    private ModelMapper modelMapper;
    @Mock
    private CustomerDao customerDao;
    @InjectMocks
    private SimpleCustomerService customerService;



    @Test
    void getAllDTOs() {
        // given
        Customer customer = EntityGenerator.getNewCustomer();
        when(customerDao.getAll()).thenReturn(List.of(customer));

        // when
        List<CustomerDTO> allDTOs = customerService.getAllDTOs();

        //then
        assertEquals(1, allDTOs.size());
        assertEquals(customer.getEmail(), allDTOs.get(0).getEmail());
        verify(customerDao, times(1)).getAll();
    }

    @Test
    void getDtoById_shouldFindExistingId() {
        // given
        Long customerId = 1L;
        Customer customer = EntityGenerator.getNewCustomer();
        customer.setId(customerId);
        when(customerDao.getById(customerId)).thenReturn(Optional.of(customer));

        // when
        CustomerDTO dto = customerService.getDtoById(customerId);

        //then
        assertEquals(dto.getId(), customerId);
        verify(customerDao, times(1)).getById(any());
    }

    @Test()
    void getDtoById_shouldThrowException() {
        // given
        Long customerId = 1L;

        // when
        when(customerDao.getById(customerId)).thenThrow(new RuntimeException("Can't find by id"));

        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> customerService.getDtoById(customerId));
        assertEquals("Can't find by id", runtimeException.getLocalizedMessage());
        verify(customerDao, times(1)).getById(any());
    }

    void save() {
    }

    void getByUsername() {
    }

    void update() {
    }
}