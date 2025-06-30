package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.spring6restmvc.model.Customer;

@Service
public class CustomerServiceImpl implements CustomerService {

    private HashMap<UUID, Customer> customersMap;

    public CustomerServiceImpl() {

        customersMap = new HashMap<>();

        Customer customer1 = Customer.builder()
                            .id(UUID.randomUUID())
                            .customerName("Customer 1")
                            .version(1234)
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();
        customersMap.put(customer1.getId(), customer1);
        Customer customer2 = Customer.builder()
                            .id(UUID.randomUUID())
                            .customerName("Customer 2")
                            .version(3456)
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();
        customersMap.put(customer2.getId(), customer2); 
        Customer customer3 = Customer.builder()
                            .id(UUID.randomUUID())
                            .version(9813)
                            .customerName("Customer 3")
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();
        customersMap.put(customer3.getId(), customer3); 
    }


    @Override
    public List<Customer> listCustomers() {
        return customersMap.values().stream().toList();        
    }

    @Override
    public Optional<Customer> getCustomerById(UUID id) {
        return Optional.of(customersMap.get(id));
    }


    @Override
    public Customer saveNewCustomer(Customer entity) {
        // TODO Auto-generated method stub
        Customer savedCustomer = Customer.builder()
                            .id(UUID.randomUUID())
                            .customerName(entity.getCustomerName())
                            .version(entity.getVersion())
                            .createdDate(LocalDateTime.now())
                            .lastModifiedDate(LocalDateTime.now())
                            .build();
        customersMap.put(savedCustomer.getId(), savedCustomer); 
        return savedCustomer;                        
    }


    @Override
    public void updateExistingCustomer(UUID customerId, Customer customer) {
        var existingCustomer = customersMap.get(customerId);
        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setLastModifiedDate(LocalDateTime.now());
        customersMap.put(customerId, existingCustomer);
    }


    @Override
    public void deleteCustomerById(UUID customerId) {
        // TODO Auto-generated method stub
        customersMap.remove(customerId);
    }


    @Override
    public void patchExistingCustomer(UUID customerId, Customer customer) {
        var existing = customersMap.get(customerId);
        if(customer.getCustomerName() != null){
            if (StringUtils.hasText(customer.getCustomerName())) {
                existing.setCustomerName(customer.getCustomerName());
            }
            existing.setLastModifiedDate(LocalDateTime.now());
            customersMap.put(customerId, existing);
        }
    }

}
