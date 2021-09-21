package com.lehansun.pet.project.speakingpractice;

import com.lehansun.pet.project.speakingpractice.model.Customer;
import com.lehansun.pet.project.speakingpractice.model.dto.CustomerDTO;
import com.lehansun.pet.project.speakingpractice.model.dto.CustomerDtoWithPassword;
import com.lehansun.pet.project.speakingpractice.service.CustomerService;
import com.lehansun.pet.project.speakingpractice.service.impl.SimpleCustomerService;
import com.lehansun.pet.project.speakingpractice.util.EntityGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.lehansun.pet.project.speakingpractice.RepositoryTests.NON_EXISTENT_USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;



@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CustomerServiceTests {

    public static final long CUSTOMER_ID = 1L;
    public static final long NON_EXISTENT_ID = 113L;
    public static final String EXISTENT_USERNAME = "TestUsername";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void getAll_shouldReturnNotEmptyList() {
        List<Customer> all = customerService.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void getById_shouldFindByExistingId() {
        Optional<Customer> byId = customerService.getById(CUSTOMER_ID);
        assertEquals(byId.get().getId(), CUSTOMER_ID);
    }

    @Test
    void getById_shouldReturnEmptyOptionalObject_WhenIdIsNonExistent() {
        Optional<Customer> byId = customerService.getById(NON_EXISTENT_ID);
        assertThat(byId.isEmpty()).isTrue();
    }

    @Test
    void save_shouldSaveCustomerAndAssignHimANewId() {
        Customer customer = EntityGenerator.getNewCustomer();
        List<Customer> customers = customerService.getAll();
        Integer numberOfRecordsBefore = customers.size();

        customerService.save(customer);
        customers = customerService.getAll();
        Integer numberOfRecordsAfter = customers.size();

        assertThat(customer.getId()).isNotNull();
        assertThat(numberOfRecordsAfter - numberOfRecordsBefore).isEqualTo(1);
    }

    @Test
    public void delete_shouldDeleteExistentCustomer() {
        Customer customer = EntityGenerator.getNewCustomer();
        Integer numberOfRecordsBeforeTest = customerService.getAll().size();

        customerService.save(customer);
        Integer numberOfRecordsAfterSaving = customerService.getAll().size();
        customerService.delete(customer);
        Integer numberOfRecordsAfterDeleting = customerService.getAll().size();
        Optional<Customer> deletedCustomer = customerService.getById(customer.getId());

        assertThat(numberOfRecordsAfterSaving - numberOfRecordsBeforeTest).isEqualTo(1);
        assertThat(numberOfRecordsBeforeTest).isEqualTo(numberOfRecordsAfterDeleting);
        assertThat(deletedCustomer.isPresent()).isFalse();
    }

    @Test
    void getAllDTOs_shouldReturnNotEmptyList() {
        List<CustomerDTO> all = customerService.getAllDTOs();
        assertEquals(2, all.size());
    }

    @Test
    void getDtoById_shouldReturnCustomerDTO_WhenIdIsCorrect() {
        CustomerDTO dto = customerService.getDtoById(CUSTOMER_ID);
        assertThat(Objects.nonNull(dto)).isTrue();
        assertThat(dto.getId()).isEqualTo(CUSTOMER_ID);
    }

    @Test
    void getDtoById_shouldThrowRuntimeException_WhenIdIsNonExistent() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> customerService.getDtoById(NON_EXISTENT_ID))
                .withMessage("Element with id:%d does not exist", NON_EXISTENT_ID);
    }

    @Test
    void getDtoByUsername_shouldReturnCustomerDTO_WhenUsernameIsCorrect() {
        CustomerDTO dtoByUsername = customerService.getDtoByUsername(EXISTENT_USERNAME);
        assertThat(Objects.nonNull(dtoByUsername)).isTrue();
        assertThat(dtoByUsername.getUsername()).isEqualTo(EXISTENT_USERNAME);
    }

    @Test
    void getDtoByUsername_shouldThrowRuntimeException_WhenUsernameIsNonExistent() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> customerService.getDtoByUsername(NON_EXISTENT_USERNAME))
                .withMessage(String.format("Element with username-%s does not exist", NON_EXISTENT_USERNAME));
    }

    @Test
    void saveByDTO_shouldSaveCustomer() {
        Customer customer = EntityGenerator.getNewCustomer();
        List<Customer> customers = customerService.getAll();
        Integer numberOfRecordsBefore = customers.size();

        customerService.saveByDTO(modelMapper.map(customer, CustomerDtoWithPassword.class));
        customers = customerService.getAll();
        Integer numberOfRecordsAfter = customers.size();

        assertThat(numberOfRecordsAfter - numberOfRecordsBefore).isEqualTo(1);
    }

    @Test
    void updateByDTO_shouldUpdateCustomer_whenCustomerExist() {
        Customer newCustomer = EntityGenerator.getNewCustomer();
        customerService.save(newCustomer);

        CustomerDTO valueBeforeUpdate = modelMapper.map(newCustomer, CustomerDTO.class);
        CustomerDTO valueToUpdate = EntityGenerator.getNewCustomerDTO();
        valueToUpdate.setId(newCustomer.getId());

        customerService.updateByDTO(newCustomer.getId(), valueToUpdate);
        CustomerDTO valueAfterUpdate = customerService.getDtoById(newCustomer.getId());

        assertThat(valueBeforeUpdate).isNotEqualByComparingTo(valueAfterUpdate);
        assertThat(valueAfterUpdate).isEqualByComparingTo(valueToUpdate);
    }

    @Test
    void updateByDTO_shouldThrowRuntimeException_WhenUpdatingCustomerWithNonExistentId() {
        CustomerDTO valueToUpdate = EntityGenerator.getNewCustomerDTO();
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> customerService.updateByDTO(NON_EXISTENT_ID, valueToUpdate))
                .withMessage(String.format("Element with id:%d does not exist", NON_EXISTENT_ID));
    }

    @Test
    void updatePassword_shouldUpdatePassword_whenUpdatingExistentCustomer() {
        Customer customer = customerService.getById(CUSTOMER_ID).get();
        String oldPassword = customer.getPassword();
        String updatedPassword = oldPassword + "UPDATED";

        customerService.updatePassword(customer.getUsername(), updatedPassword);

        assertThat(customerService.getById(customer.getId()).get().getPassword()).isEqualTo(updatedPassword);
    }
}

