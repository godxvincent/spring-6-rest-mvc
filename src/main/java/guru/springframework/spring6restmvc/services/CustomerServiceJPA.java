package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.spring6restmvc.entities.Customer;
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
        return this.customerRepository.findAll().stream().map(customerMapper::customerToCustomerDTO).toList();
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(this.customerMapper.customerToCustomerDTO(this.customerRepository.findById(id).orElse(null)));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO entity) {
        Customer newCustomer = this.customerMapper.customerDTOToCustomer(entity);
        return this.customerMapper.customerToCustomerDTO(this.customerRepository.save(newCustomer));
        
    }

    @Override
    public Optional<CustomerDTO> updateExistingCustomer(UUID customerId, CustomerDTO customer) {
        
        AtomicReference<Optional<CustomerDTO>> updatedCustomer = new AtomicReference<>(Optional.empty());

        this.customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {
            foundCustomer.setCustomerName(customer.getCustomerName());
            updatedCustomer.set(Optional.of(this.customerMapper.customerToCustomerDTO(this.customerRepository.save(foundCustomer))));
        }, () -> {
            updatedCustomer.set(Optional.empty());
        });
        return updatedCustomer.get();

    }

    @Override
    public Boolean deleteCustomerById(UUID customerId) {
        if (this.customerRepository.existsById(customerId)) {
            this.customerRepository.deleteById(customerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<CustomerDTO> patchExistingCustomer(UUID customerId, CustomerDTO customer) {
        
        AtomicReference<Optional<CustomerDTO>> updatedCustomer = new AtomicReference<>(Optional.empty());

        this.customerRepository.findById(customerId).ifPresentOrElse(foundCustomer -> {

            if (StringUtils.hasText(customer.getCustomerName()) && !customer.getCustomerName().equals(foundCustomer.getCustomerName())) {
                foundCustomer.setCustomerName(customer.getCustomerName());
                updatedCustomer.set(Optional.of(this.customerMapper.customerToCustomerDTO(this.customerRepository.save(foundCustomer))));
            } else {
                updatedCustomer.set(Optional.empty());
            }
        }, () -> {
            updatedCustomer.set(Optional.empty());
        });
        return updatedCustomer.get();
    }

}
