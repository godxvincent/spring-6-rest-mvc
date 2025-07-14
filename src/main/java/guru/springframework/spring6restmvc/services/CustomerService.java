package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.CustomerDTO;

public interface CustomerService {

    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);
    CustomerDTO saveNewCustomer(CustomerDTO entity);
    void updateExistingCustomer(UUID customerId, CustomerDTO customer);
    void deleteCustomerById(UUID customerId);
    void patchExistingCustomer(UUID customerId, CustomerDTO customer);

}
