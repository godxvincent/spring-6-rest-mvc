package guru.springframework.spring6restmvc.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.entities.BeerOrder;
import guru.springframework.spring6restmvc.entities.BeerOrderShipment;
import guru.springframework.spring6restmvc.entities.Customer;
import jakarta.transaction.Transactional;

@SpringBootTest
public class BeerOrderRepositoryTest {

    @Autowired
    private BeerOrderRepository beerOrderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BeerRepository beerRepository;

    private Customer testCustomer;
    private Beer testBeer;


    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().get(0);
        testBeer = beerRepository.findAll().get(0);
    }

    @Transactional
    @Test
    public void testSaveBeerOrder() {
        System.out.println(beerOrderRepository.findAll().size());
        System.out.println(beerRepository.findAll().size());
        System.out.println(customerRepository.findAll().size());
        System.out.println(testBeer.getBeerName());
        System.out.println(testCustomer.getCustomerName());
        BeerOrder beerOrder = BeerOrder.builder()
                        .customerRef("Test of reference")
                        .beerOrderLines(null)
                        .beerOrderShipment(BeerOrderShipment.builder().trackingNumber("test1234").build())
                        .customer(testCustomer).build();
        // If we use flush we force hibernate to save all immediately causing performance issues.
        // BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        System.out.println(savedBeerOrder.getCustomerRef());
            
    }

}
