package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.Customer;

public interface CustomerService {

    List<Customer> listCustomers();
    Customer getCustomerById(UUID id);

}
