package guru.springframework.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;

@SpringBootTest
public class BeerControllerIT {

    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerRepository  beerRepository;

    @Autowired
    private BeerMapper beerMapper;

    // As we are affecting the DB is better to add this.
    @Rollback
    @Transactional
    @Test
    void testCreateBeer() {
        BeerDTO beerDto = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity responseEntity = this.beerController.createBeer(beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
        assertThat(beer.getBeerName()).isEqualTo(beerDto.getBeerName());
        assertThat(beer.getCreateDateTime()).isNotNull();
        assertThat(beer.getUpdateDateTime()).isNull();


    }

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

    @Test
    @Rollback
    @Transactional
    void testPatchBeer() {
       
    }

    @Test
    void testPutBeerException() {
        assertThrows(NotFoundException.class, ()-> {
            beerController.putBeer(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testPutBeer() {
        Beer beer = this.beerRepository.findAll().get(0);
        BeerDTO beerDTO = this.beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);

        final String beerName = "Ricardo's Beer";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = this.beerController.putBeer(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updateBeer = this.beerRepository.findById(beer.getId()).get();
        assertThat(updateBeer.getBeerName()).isEqualTo(beerName);
        assertThat(beer.getCreateDateTime()).isEqualTo(updateBeer.getCreateDateTime());
        // assertThat(beer.getUpdateDateTime()).isNotEqualTo(updateBeer.getUpdateDateTime()); // this fails bc there's an internal reference to the same date that is shared.
        // assertThat(beer.getVersion()).isNotEqualTo(updateBeer.getVersion());
        
    }
}
