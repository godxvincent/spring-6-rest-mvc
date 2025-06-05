package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customer")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @PatchMapping("{customerId}")
    public ResponseEntity patchExistingCustomer(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {

        customerService.patchExistingCustomer(customerId, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity deleteMethodName(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomerById(customerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{customerId}")
    public ResponseEntity putMethodName(@PathVariable("customerId") UUID customerId, @RequestBody Customer customer) {
        //TODO: process PUT request
        
        customerService.updateExistingCustomer(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @PostMapping()
    public ResponseEntity createCustomer(@RequestBody Customer entity) {
        
        Customer customer = customerService.saveNewCustomer(entity);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + customer.getId().toString());
        
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    

    @RequestMapping(method = RequestMethod.GET)
    public List<Customer> listCustomers(){
        return customerService.listCustomers();
    }

    @RequestMapping(value = "{customerId}", method = RequestMethod.GET)
    public Customer getcustomerById(@PathVariable("customerId") UUID customerId){

        log.debug("Get Customer by Id - in controlle - test");

        return customerService.getCustomerById(customerId);
    }

}
