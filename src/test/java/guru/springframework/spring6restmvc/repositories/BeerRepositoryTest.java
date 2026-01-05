package guru.springframework.spring6restmvc.repositories;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        // Beer savedBeer = beerRepository.save(beer);
        var savedBeer = beerRepository.save(beer);
        beerRepository.flush();
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();

    }

    @Test
    void testSaveBeerTooLongName() {

        assertThrows(ConstraintViolationException.class, () -> {
            var beer = Beer.builder()
                        .beerName("test beer name 0123456789001234567890012345678900123456789001234567890")
                        .beerStyle(BeerStyle.ALE)
                        .upc("Not sure")
                        .price(new BigDecimal(15))
                        .quantityOnHand(200)
                        .build();
            // Beer savedBeer = beerRepository.save(beer);
            var savedBeer = beerRepository.save(beer);
            beerRepository.flush();
        });
        

    }
}
