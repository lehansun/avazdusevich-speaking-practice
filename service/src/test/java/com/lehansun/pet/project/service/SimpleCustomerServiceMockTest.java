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
class SimpleCustomerServiceMockTest {

    @Spy
    private ModelMapper modelMapper;
    @Mock
    private CustomerDao mockDao;
    @InjectMocks
    private SimpleCustomerService testingService;

    @Test
    void getAllDTOs() {
        // given
        Customer customer = EntityGenerator.getNewCustomer();
        when(mockDao.getAll()).thenReturn(List.of(customer));

        // when
        List<CustomerDTO> allDTOs = testingService.getAllDTOs();

        //then
        assertEquals(1, allDTOs.size());
        assertEquals(customer.getEmail(), allDTOs.get(0).getEmail());
        verify(mockDao, times(1)).getAll();
    }

    @Test
    void getDtoById_shouldFindExistingId() {
        // given
        long customerId = 1L;
        Customer customer = EntityGenerator.getNewCustomer();
        customer.setId(customerId);
        when(mockDao.getById(customerId)).thenReturn(Optional.of(customer));

        // when
        CustomerDTO dto = testingService.getDtoById(customerId);

        //then
        assertEquals(dto.getId(), customerId);
        verify(mockDao, times(1)).getById(any());
    }

    @Test()
    void getDtoById_shouldFailOnFindingWithNonExistentId() {
        // given
        long customerId = -1L;
        String message = "Element with id:-1 does not exist";

        // when
        when(mockDao.getById(customerId)).thenReturn(Optional.empty());

        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> testingService.getDtoById(customerId));
        assertEquals(message, runtimeException.getLocalizedMessage());
        verify(mockDao, times(1)).getById(any());
    }

    @Test
    void getByUsername_shouldFindCustomer() {
        // given
        Customer customer = EntityGenerator.getNewCustomer();
        String customerUsername = customer.getUsername();
        when(mockDao.getByUsername(customerUsername)).thenReturn(Optional.of(customer));

        // when
        CustomerDTO dto = testingService.getDtoByUsername(customerUsername);

        //then
        assertEquals(dto.getUsername(), customerUsername);
        verify(mockDao, times(1)).getByUsername(anyString());
    }

    @Test
    void getByUsername_shouldFailOnFindingWithNonExistentUsername() {
        // given
        String customerUsername = "Non-existent username";
        String message = "Element with username-Non-existent username does not exist";

        // when
        when(mockDao.getByUsername(customerUsername)).thenReturn(Optional.empty());

        //then
        RuntimeException runtimeException = assertThrows(RuntimeException.class,
                () -> testingService.getDtoByUsername(customerUsername));
        assertEquals(message, runtimeException.getLocalizedMessage());
        verify(mockDao, times(1)).getByUsername(anyString());
    }

    @Test
    void save() {
        // given
        Customer customer = EntityGenerator.getNewCustomer();

        // when
        testingService.save(customer);

        //then
        verify(mockDao, times(1)).save(customer);
    }

    @Test
    void update() {
        // given
        Customer customer = EntityGenerator.getNewCustomer();

        // when
        testingService.update(customer);

        //then
        verify(mockDao, times(1)).update(customer);
    }

    @Test
    void updatePassword() {
        // when
        testingService.updatePassword("test", "test");

        //then
        verify(mockDao, times(1)).updatePassword("test", "test");
        verifyNoMoreInteractions(mockDao);
    }
}