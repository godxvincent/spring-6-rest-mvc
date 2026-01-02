package guru.springframework.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
public class CustomerControllerIT {

    @Autowired
    private CustomerController customerController;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    @Rollback
    @Transactional
    void testCreateCustomer() {
        CustomerDTO toSaveCustomer = CustomerDTO.builder().customerName("Ricardo Test").build();
        ResponseEntity<Void> responseEntity = this.customerController.createCustomer(toSaveCustomer);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        
        String[] locatorInfo = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedCustomerUUID = UUID.fromString(locatorInfo[4]);

        Customer savedCustomer = this.customerRepository.findById(savedCustomerUUID).get();
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getCustomerName()).isEqualTo(toSaveCustomer.getCustomerName());
        assertThat(savedCustomer.getVersion()).isEqualTo(0);
        assertThat(savedCustomer.getCreateDateTime()).isNotNull();
        assertThat(savedCustomer.getUpdateDateTime()).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();

    }

    @Test
    @Rollback
    @Transactional
    void testDeleteCustomerById() {
        Customer savedCustomer = this.customerRepository.findAll().get(0);
        ResponseEntity responseEntity = this.customerController.deleteCustomerById(savedCustomer.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(this.customerRepository.findById(savedCustomer.getId()).isEmpty()).isTrue();
        assertThat(this.customerRepository.findAll().size()).isEqualTo(2);

    }

    @Test
    void testDeleteCustomerByIdNotFound()
    {
        assertThrows(NotFoundException.class, () -> {
            this.customerController.deleteCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testGetcustomerById() {
        var customer = this.customerRepository.findAll().get(0);
        var testCustomer = this.customerController.getcustomerById(customer.getId());
        assertThat(testCustomer.getCustomerName()).isEqualTo(customer.getCustomerName());
        assertThat(testCustomer.getVersion()).isEqualTo(customer.getVersion());
        assertThat(testCustomer.getCreateDateTime()).isEqualTo(customer.getCreateDateTime());
        assertThat(testCustomer.getUpdateDateTime()).isEqualTo(customer.getUpdateDateTime());
    }

    @Test
    void testNotFoundGetcustomerById() {
        assertThrows(NotFoundException.class, () -> {
            this.customerController.getcustomerById(UUID.randomUUID());
        });
    }

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
    void testPutcustomerByIdNotFound() {
        assertThrows(NotFoundException.class, ()-> {
            this.customerController.putCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testPutCustomer() {
        Customer customer = this.customerRepository.findAll().get(0);
        CustomerDTO customerDTO = this.customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);

        final String customerName = "Ricardo Vargas";
        customerDTO.setCustomerName(customerName);

        ResponseEntity<Void> responseEntity = this.customerController.putCustomerById(customer.getId(), customerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updatecustomer = this.customerRepository.findById(customer.getId()).get();
        assertThat(updatecustomer.getCustomerName()).isEqualTo(customerName);
        assertThat(customer.getCreateDateTime()).isEqualTo(updatecustomer.getCreateDateTime());
        
    }


    @Test
    void testPatchCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, ()-> {
            this.customerController.patchExistingCustomer(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testPatchCustomer() {
        Customer customer = this.customerRepository.findAll().get(0);
        CustomerDTO customerDTO = this.customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);

        // Name shouldn't be changed.
        final String customerName = customerDTO.getCustomerName();
        customerDTO.setCustomerName(customerName);

        assertThrows(NotFoundException.class, ()-> {
            this.customerController.patchExistingCustomer(customer.getId(), customerDTO);
        });

        // assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        // var updatecustomer = this.customerRepository.findById(customer.getId()).get();
        // assertThat(updatecustomer.getCustomerName()).isEqualTo(customerName);
        // assertThat(customer.getCreateDateTime()).isEqualTo(updatecustomer.getCreateDateTime());
        
    }



}
