package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@AllArgsConstructor
@Slf4j
public class CustomerController {


    public final static String CUSTOMER_PATH = "/api/v1/customer";
    public final static String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";


    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchExistingCustomer(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {

        customerService.patchExistingCustomer(customerId, customer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteMethodName(@PathVariable("customerId") UUID customerId) {
        customerService.deleteCustomerById(customerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity putMethodName(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDTO customer) {
        //TODO: process PUT request
        
        customerService.updateExistingCustomer(customerId, customer);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity createCustomer(@RequestBody CustomerDTO entity) {
        
        CustomerDTO customer = customerService.saveNewCustomer(entity);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customer/" + customer.getId().toString());
        
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
    

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers(){
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getcustomerById(@PathVariable("customerId") UUID customerId){

        log.debug("Get Customer by Id - in controlle - test");

        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

}
