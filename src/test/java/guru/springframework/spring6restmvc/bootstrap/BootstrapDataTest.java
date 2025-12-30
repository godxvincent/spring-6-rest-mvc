package guru.springframework.spring6restmvc.bootstrap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;

@DataJpaTest
public class BootstrapDataTest {


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BeerRepository beerRepository;

    @Test
    void testRun() {
        var bootstrap = new BootstrapData(beerRepository, customerRepository);
        try {
            bootstrap.run(null, null);
            var beerCount = beerRepository.count();
            var customerCount = customerRepository.count();
            assertEquals(3, beerCount);
            assertEquals(3, customerCount);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
