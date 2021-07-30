package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.dao.config.DaoTestConfig;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.util.EntityGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(classes = DaoTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class CustomerJpaDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void getAll() {
        log.info("IN getAll()");
        // when
        List<Customer> customers = customerDao.getAll();

        //then
        assertNotNull(customers);
        assertTrue(customers.size() > 0);
    }

    @Test
    public void getById() {
        log.info("IN getById()");
        // when
        Optional<Customer> byId = customerDao.getById(1L);

        // then
        assertTrue(byId.isPresent());
        assertEquals(customerDao.getAll().get(0), byId.get());
    }

    @Test
    void getById_shouldReturnEmptyOptionalObject() {
        log.info("IN getById_shouldReturnEmptyOptionalObject()");
        // when
        Optional<Customer> byId = customerDao.getById(100L);

        // then
        assertTrue(byId.isEmpty());
    }

    @Test
    void save() {
        log.info("IN save()");
        // given
        Customer customer = EntityGenerator.getNewCustomer();
        List<Customer> customers = customerDao.getAll();
        Integer numberOfRecordsBefore = customers.size();

        // when
        customerDao.save(customer);
        customers = customerDao.getAll();
        Integer numberOfRecordsAfter = customers.size();

        //then
        assertNotNull(customer.getId());
        assertEquals(1, (numberOfRecordsAfter - numberOfRecordsBefore));
    }

    @Test
    public void update() {
        log.info("IN update()");

        // given
        Customer customer = EntityGenerator.getNewCustomer();
        customerDao.save(customer);
        String newName = "TestName";

        // when
        customer.setFirstname(newName);
        customerDao.update(customer);
        customer = customerDao.getById(customer.getId()).get();

        // then
        assertEquals(newName, customer.getFirstname());
    }

    @Test
    public void delete() {
        log.info("IN delete()");
        // given
        Customer customer = EntityGenerator.getNewCustomer();
        Integer numberOfRecordsBefore = customerDao.getAll().size();


        // when
        customerDao.save(customer);
        Integer numberOfRecordsAfterSaving = customerDao.getAll().size();
        customerDao.delete(customer);
        Integer numberOfRecordsAfterDeleting = customerDao.getAll().size();
        Optional<Customer> byId = customerDao.getById(customer.getId());

        //then
        assertEquals(1, (numberOfRecordsAfterSaving - numberOfRecordsBefore));
        assertEquals(numberOfRecordsBefore, numberOfRecordsAfterDeleting);
        assertFalse(byId.isPresent());
    }

    @Test
    public void getByUsername_shouldFindUser() {
        log.info("IN getByUsername_shouldFindUser() test.");

        // when
        Optional<Customer> byUsername = customerDao.getByUsername("Lehansun");

        // then
        assertTrue(byUsername.isPresent());
        assertEquals(customerDao.getAll().get(0), byUsername.get());
    }

    @Test
    public void getByUsername_shouldReturnEmptyOptionalObject() {
        log.info("IN getByUsername_shouldReturnEmptyOptionalObject() test.");

        // when
        Optional<Customer> byUsername = customerDao.getByUsername("NonExistentUsername");

        // then
        assertTrue(byUsername.isEmpty());
    }


    @Test
    void update_shouldUpdatePassword() {
        String username = "Lehansun";
        String testPassword = "testPassword";
        String oldPassword = customerDao.getByUsername(username).get().getPassword();

        // when
        customerDao.updatePassword(username, testPassword);
        String newPassword = customerDao.getByUsername(username).get().getPassword();

        // then
        assertNotEquals(oldPassword, newPassword);
        assertEquals(testPassword, newPassword);
    }

    @Test
    void  update_shouldFailOnPasswordUpdating() {
        // given
        String username = "NonExistentUsername";
        String password = "password";
        String message = "Incorrect username-" + username;

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> customerDao.updatePassword(username, password));

        // then
        assertEquals(message, exception.getLocalizedMessage());
    }
}
