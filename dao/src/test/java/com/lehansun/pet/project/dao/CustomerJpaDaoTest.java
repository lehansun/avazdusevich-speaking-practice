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
    void shouldReturnNullWhenFindByNonexistentId() {
        log.info("IN shouldReturnNullWhenFindByNonexistentId()");
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

        // when
        String newName = "TestName";
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



}
