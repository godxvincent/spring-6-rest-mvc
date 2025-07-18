package guru.springframework.spring6restmvc.repositories;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
// import guru.springframework.spring6restmvc.repositories.BeerRepository;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BeerRepositoryTest {

    
    @Autowired
    BeerRepository beerRepository;


    @Test
    void testSaveBeer() {
        var beer = Beer.builder()
                    .beerName("test beear name")
                    .beerStyle(BeerStyle.ALE)
                    .upc("Not sure")
                    .price(new BigDecimal(15))
                    .quantityOnHand(200)
                    .build();
        Beer savedBeer = beerRepository.save(beer);
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();

    }
}
