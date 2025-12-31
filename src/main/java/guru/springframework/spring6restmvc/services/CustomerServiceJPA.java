package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.mappers.CustomerMapper;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJPA implements CustomerService{


    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> listCustomers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listCustomers'");
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerById'");
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO entity) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveNewCustomer'");
    }

    @Override
    public void updateExistingCustomer(UUID customerId, CustomerDTO customer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateExistingCustomer'");
    }

    @Override
    public void deleteCustomerById(UUID customerId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteCustomerById'");
    }

    @Override
    public void patchExistingCustomer(UUID customerId, CustomerDTO customer) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'patchExistingCustomer'");
    }

}
