package com.lehansun.pet.project.speakingpractice;

import com.lehansun.pet.project.speakingpractice.model.Customer;
import com.lehansun.pet.project.speakingpractice.repository.CustomerRepository;
import com.lehansun.pet.project.speakingpractice.util.EntityGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
class RepositoryTests {

	public static final long NON_EXISTENT_ID = 113L;
	public static final String NON_EXISTENT_USERNAME = "NonExistentUsername";
	public static final String EXISTENT_USERNAME = "TestUsername";

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void findAll_shouldReturnNotEmptyList() {
		List<Customer> all = customerRepository.findAll();
		assertThat(all.size()).isGreaterThan(0);
	}

	@Test
	void findById_shouldReturnCustomer_WhenIdIsCorrect() {
		Customer byId = customerRepository.getById(1L);
		assertThat(byId.getId()).isEqualTo(1L);
	}

	@Test
	void findById_shouldReturnEmptyOptionalObject_WhenIdIsNonExistent() {
		Optional<Customer> byId = customerRepository.findById(NON_EXISTENT_ID);
		assertThat(byId.isEmpty()).isTrue();
	}

	@Test
	void findByUsername_shouldReturnCustomer_WhenUsernameIsCorrect() {
		Optional<Customer> byUsername = customerRepository.findByUsername(EXISTENT_USERNAME);
		assertThat(byUsername.isPresent()).isTrue();
		assertThat(byUsername.get().getUsername()).isEqualTo(EXISTENT_USERNAME);
	}
	@Test
	void findByUsername_shouldReturnEmptyOptionalObject_WhenUsernameIsNonExistent() {
		Optional<Customer> byUsername = customerRepository.findByUsername(NON_EXISTENT_USERNAME);
		assertThat(byUsername.isEmpty()).isTrue();
	}

	@Test
	void save_shouldSaveCustomerAndAssignHimANewId() {
		Customer customer = EntityGenerator.getNewCustomer();
		List<Customer> customers = customerRepository.findAll();
		Integer numberOfRecordsBefore = customers.size();

		customerRepository.save(customer);
		customers = customerRepository.findAll();
		Integer numberOfRecordsAfter = customers.size();

		assertThat(customer.getId()).isNotNull();
		assertThat(numberOfRecordsAfter - numberOfRecordsBefore).isEqualTo(1);
	}

	@Test
	public void delete_shouldDeleteExistentCustomer() {
		Customer customer = EntityGenerator.getNewCustomer();
		Integer numberOfRecordsBeforeTest = customerRepository.findAll().size();

		customerRepository.save(customer);
		Integer numberOfRecordsAfterSaving = customerRepository.findAll().size();
		customerRepository.delete(customer);
		Integer numberOfRecordsAfterDeleting = customerRepository.findAll().size();
		Optional<Customer> deletedCustomer = customerRepository.findById(customer.getId());

		assertThat(numberOfRecordsAfterSaving - numberOfRecordsBeforeTest).isEqualTo(1);
		assertThat(numberOfRecordsBeforeTest).isEqualTo(numberOfRecordsAfterDeleting);
		assertThat(deletedCustomer.isPresent()).isFalse();
	}

}
