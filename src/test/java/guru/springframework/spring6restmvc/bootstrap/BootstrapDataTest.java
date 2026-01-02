package guru.springframework.spring6restmvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
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

    private BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository);
    }


    @Test
    void testRun() {
        
        try {
            this.bootstrapData.run(null, null);
            var beerCount = beerRepository.count();
            var customerCount = customerRepository.count();
            assertThat(beerCount).isEqualTo(3);
            assertThat(customerCount).isEqualTo(3);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
