package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

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
    public Customer getCustomerById(UUID id) {
        return customersMap.get(id);
    }

}
