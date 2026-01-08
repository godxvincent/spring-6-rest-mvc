package guru.springframework.spring6restmvc.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.BeerCSVService;
import guru.springframework.spring6restmvc.services.BeerCSVServiceImpl;

@DataJpaTest
@Import(BeerCSVServiceImpl.class)
public class BootstrapDataTest {


    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BeerRepository beerRepository;
    @Autowired
    private BeerCSVService beerCSVService;

    private BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCSVService);
    }


    @Test
    void testRun() {
        
        try {
            this.bootstrapData.run(null, null);
            var beerCount = beerRepository.count();
            var customerCount = customerRepository.count();
            assertThat(beerCount).isEqualTo(2413);
            assertThat(customerCount).isEqualTo(3);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
