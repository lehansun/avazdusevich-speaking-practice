package com.lehansun.pet.project.dao;

import com.lehansun.pet.project.api.dao.CustomerDao;
import com.lehansun.pet.project.dao.config.TestConfig;
import com.lehansun.pet.project.model.Customer;
import com.lehansun.pet.project.model.Language;
import com.lehansun.pet.project.model.Role;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class CustomerJpaDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    void save() {
        // given
        Customer customer = getNewCustomer();
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
    public void getAll() {
        // when
        List<Customer> customers = customerDao.getAll();

        //then
        assertNotNull(customers);
        assertTrue(customers.size() > 0);
    }

    @Test
    public void getById() {
        // given
        Customer customer = getNewCustomer();
        customerDao.save(customer);

        // when
        Optional<Customer> byId = customerDao.getById(customer.getId());

        // then
        assertTrue(byId.isPresent());
        assertEquals(customer, byId.get());
    }

    @Test
    public void update() {
        // given
        Customer customer = getNewCustomer();
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
        // given
        Customer customer = getNewCustomer();
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

    private Customer getNewCustomer() {
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

    private Language getNativeLanguage() {
        Language language = new Language();
        language.setName("Russian");
        language.setId(1l);
        return language;
    }

    private Language getLearningLanguage() {
        Language language = new Language();
        language.setName("English");
        language.setId(2l);
        return language;
    }

    private List<Role> getRoles() {
        List<Role> roles = new ArrayList<>();
        Role role = new Role();
        role.setName("USER");
        role.setId(1l);
        roles.add(role);
        return roles;
    }

}
