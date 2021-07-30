package com.lehansun.pet.project.security.service;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.util.EntityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceMockTest {

    @Mock
    private CustomerDao customerDao;
    @InjectMocks
    CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetailsObject() {
        // given
        Customer customer = EntityGenerator.getNewCustomer();
        String username = customer.getUsername();

        // when
        when(customerDao.getByUsername(username)).thenReturn(Optional.of(customer));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // then
        assertEquals(username, userDetails.getUsername());
        assertEquals(customer.getPassword(), userDetails.getPassword());
        verify(customerDao, times(1)).getByUsername(username);
    }

    @Test
    void loadUserByUsername_shouldFailOnFindingWithNonExistentUsername() {
        // given
        String customerUsername = "Non_existent_username";
        String message = "Customer don't exist. Username - Non_existent_username.";

        // when
        when(customerDao.getByUsername(customerUsername)).thenReturn(Optional.empty());

        //then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userDetailsService.loadUserByUsername(customerUsername));
        assertEquals(message, exception.getLocalizedMessage());
        verify(customerDao, times(1)).getByUsername(anyString());
    }
}