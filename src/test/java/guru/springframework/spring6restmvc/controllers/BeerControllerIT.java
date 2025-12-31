package guru.springframework.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.repositories.BeerRepository;

@SpringBootTest
public class BeerControllerIT {

    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerRepository  beerRepository;

    // @Test
    // void testCreateBeer() {

    // }

    // @Test
    // void testDeleteBeer() {

    // }

    @Test
    void testGetBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        var testBeer = beerController.getBeerById(beer.getId());
        assertThat(testBeer.getBeerName()).isEqualTo(beer.getBeerName());
        assertThat(testBeer.getBeerStyle()).isEqualTo(beer.getBeerStyle());
        assertThat(testBeer.getPrice()).isEqualTo(beer.getPrice());
        assertThat(testBeer.getQuantityOnHand()).isEqualTo(beer.getQuantityOnHand());
        assertThat(testBeer.getVersion()).isEqualTo(beer.getVersion());
        assertThat(testBeer.getUpc()).isEqualTo(beer.getUpc());
    }

    @Test
    void testNotFoundGetBeerById() {
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Test
    void testListBeers() {
        var totalBeers = beerController.listBeers().size();
        assertThat(totalBeers).isEqualTo(3);
    }

    // The way we are simulating the db is empty is deleting the records that were loaded by the bootstrap file.
    // However, it's needed to tag this method with transactional and rollback as this test runs before 
    // the testListBeers() method causing an error bc the database changed its state.
    @Transactional
    @Rollback
    @Test
    void testEmptyBeers() {
        beerRepository.deleteAll();
        var totalBeers = beerController.listBeers().size();
        assertThat(totalBeers).isEqualTo(0);
    }

    // @Test
    // void testPatchBeer() {

    // }

    // @Test
    // void testPutBeer() {

    // }
}
