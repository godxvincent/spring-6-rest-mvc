package guru.springframework.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
public class CustomerControllerIT {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testListCustomers() {

        var totalCustomers = this.customerRepository.findAll().size();
        assertThat(totalCustomers).isEqualTo(3);
    }

    @Test 
    @Rollback
    @Transactional
    void testEmtpyListCustomers() {
        this.customerRepository.deleteAll();
        var totalCustomers = this.customerRepository.findAll().size();
        assertThat(totalCustomers).isEqualTo(0);
    }

    @Test
    void testGetcustomerById() {
        var customer = this.customerRepository.findAll().get(0);
        var testCustomer = this.customerController.getcustomerById(customer.getId());
        assertThat(testCustomer.getCustomerName()).isEqualTo(customer.getCustomerName());
        assertThat(testCustomer.getVersion()).isEqualTo(customer.getVersion());
        assertThat(testCustomer.getCreatedDate()).isEqualTo(customer.getCreatedDate());
        assertThat(testCustomer.getLastModifiedDate()).isEqualTo(customer.getLastModifiedDate());
    }

    @Test
    void testNotFoundGetcustomerById() {
        assertThrows(NotFoundException.class, () -> {
            this.customerController.getcustomerById(UUID.randomUUID());
        });
    }



}
