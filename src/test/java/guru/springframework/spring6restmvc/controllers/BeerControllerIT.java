package guru.springframework.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;

@SpringBootTest
public class BeerControllerIT {

    @Autowired
    private BeerController beerController;

    @Autowired
    private BeerRepository  beerRepository;

    @Autowired
    private BeerMapper beerMapper;


    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }


    @Test
    void tesListBeersByStyleAndNameShowInventoryTruePage1() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(50)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryTrue() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "true")
                        .queryParam("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.notNullValue()));
    }

    @Test
    void tesListBeersByStyleAndNameShowInventoryFalse() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("showInventory", "false")
                        .queryParam("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)))
                .andExpect(jsonPath("$.content.[0].quantityOnHand").value(IsNull.nullValue()));
    }

    @Test
    void tesListBeersByStyleAndName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageSize", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(310)));
    }


    @Test
    void testListBeersByName() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("pageSize", "1000"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content.size()", is(336)));
    }


    @Test
    void testListBeersByBeerStyle() throws Exception {
        mockMvc.perform(get(BeerController.BEER_PATH)
                        .queryParam("beerStyle", BeerStyle.ALE.name())
                        .queryParam("pageSize", "1000"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content.size()", is(400)));
    }


    @Test
    void testPatchBeerTooLongBeerName() throws Exception {
        var testBeer = this.beerRepository.findAll().get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 12345678901234567890123456789012345678901234567890");

        // given(beerService.patchExistingBeer(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(testBeer));


        MvcResult mvcResult= mockMvc.perform(patch(BeerController.BEER_PATH_ID, testBeer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());

        // verify(beerService).patchExistingBeer(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        // assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        // assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
        // assertNull(beerArgumentCaptor.getValue().getPrice());

    }


    // As we are affecting the DB is better to add this.
    @Rollback
    @Transactional
    @Test
    void testCreateBeer() {
        BeerDTO beerDto = BeerDTO.builder()
                .beerName("New Beer")
                .build();

        ResponseEntity<BeerDTO> responseEntity = this.beerController.createBeer(beerDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        
        @SuppressWarnings("null")
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
        assertThat(beer.getBeerName()).isEqualTo(beerDto.getBeerName());
        // assertThat(beer.getCreateDateTime()).isNotNull();
        assertThat(beer.getUpdateDateTime()).isNull();


    }

    @Test
    @Rollback
    @Transactional
    void testDeleteBeerById() {

        var beerToDelete = this.beerRepository.findAll().get(0);

        ResponseEntity responseEntity =  this.beerController.deleteBeer(beerToDelete.getId());

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(this.beerRepository.findById(beerToDelete.getId()).isEmpty()).isTrue();

    }

    @Test
    @Rollback
    @Transactional
    void testDeleteBeerByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            this.beerController.deleteBeer(UUID.randomUUID());
        });
        

    }
    
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
        var totalBeers = beerController.listBeers(null, null, false, 1, 2000).getContent().size();
        assertThat(totalBeers).isEqualTo(1000);
    }

    // The way we are simulating the db is empty is deleting the records that were loaded by the bootstrap file.
    // However, it's needed to tag this method with transactional and rollback as this test runs before 
    // the testListBeers() method causing an error bc the database changed its state.
    @Transactional
    @Rollback
    @Test
    void testEmptyBeers() {
        beerRepository.deleteAll();
        var totalBeers = beerController.listBeers(null, null, false, 1, 1).getContent().size();
        assertThat(totalBeers).isEqualTo(0);
    }



    @Test
    void testPutBeerByIdNotFound() {
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

        ResponseEntity<BeerDTO> responseEntity = this.beerController.putBeer(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updateBeer = this.beerRepository.findById(beer.getId()).get();
        assertThat(updateBeer.getBeerName()).isEqualTo(beerName);
        assertThat(beer.getCreateDateTime()).isEqualTo(updateBeer.getCreateDateTime());
        // assertThat(beer.getUpdateDateTime()).isNotEqualTo(updateBeer.getUpdateDateTime()); // this fails bc there's an internal reference to the same date that is shared.
        // assertThat(beer.getVersion()).isNotEqualTo(updateBeer.getVersion());
        
    }


    @Test
    void testPatchBeerByIdNotFound() {
        assertThrows(NotFoundException.class, ()-> {
            beerController.patchBeer(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Transactional
    @Rollback
    @Test
    void testPatchBeer() {
        Beer beer = this.beerRepository.findAll().get(0);
        BeerDTO beerDTO = this.beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);

        final String beerName = "Ricardo's Beer";
        beerDTO.setBeerName(beerName);

        ResponseEntity<BeerDTO> responseEntity = this.beerController.patchBeer(beer.getId(), beerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        var updateBeer = this.beerRepository.findById(beer.getId()).get();
        assertThat(updateBeer.getBeerName()).isEqualTo(beerName);
        assertThat(beer.getCreateDateTime()).isEqualTo(updateBeer.getCreateDateTime());
        
    }


}
