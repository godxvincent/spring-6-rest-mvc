package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.CustomerDTO;

public interface CustomerService {

    List<CustomerDTO> listCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);
    CustomerDTO saveNewCustomer(CustomerDTO entity);
    Optional<CustomerDTO> updateExistingCustomer(UUID customerId, CustomerDTO customer);
    Boolean deleteCustomerById(UUID customerId);
    Optional<CustomerDTO> patchExistingCustomer(UUID customerId, CustomerDTO customer);

}
