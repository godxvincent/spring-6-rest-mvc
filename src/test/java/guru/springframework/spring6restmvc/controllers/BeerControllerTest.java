package guru.springframework.spring6restmvc.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.assertj.core.api.Assertions.assertThat;




// @SpringBootTest inicializa el servidor por uno, pero aqui queremos probar como se inyectan/mock cosas con mockito.
// @SpringBootTest
@WebMvcTest(BeerController.class)
public class BeerControllerTest {

    // Como ya no hay SpringBootTest ya no va a pedirle al contexto de sprint que inyecte este controller.
    // @Autowired
    // BeerController beerController;
    @Autowired
    MockMvc mockMvc;
 
    // MockBean era la implementación antigua para indicarle al test que ese componente lo debia mockear.
    // @MockBean
    @MockitoBean
    BeerService beerService;

    // Podemos crear un object mapper a mano pero al hacerlo así dejamos que sprintboot inyecte por nosotros su propio object mapper con
    // las mismas configuraciones que usaria en el servidor real.
    @Autowired
    ObjectMapper objectMapper;

    // Incluimos aqui el servicio real ya que esta retornando data mock de cualquier forma
    // BeerServiceImpl beerServiceImpl = new BeerServiceImpl();
    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<BeerDTO> beerArgumentCaptor;

    @BeforeEach
    void setUp() throws Exception {
        beerServiceImpl = new BeerServiceImpl();

    }

    @Test
    void testNotFoundGetBeerById() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());
        mockMvc.perform(get(BeerController.BEER_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPatchBeer() throws Exception {
        var testBeer = beerServiceImpl.listBeers(null).get(0);

        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name");

        given(beerService.patchExistingBeer(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(testBeer));


        mockMvc.perform(patch(BeerController.BEER_PATH_ID, testBeer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(beerService).patchExistingBeer(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
        assertNull(beerArgumentCaptor.getValue().getPrice());

    }

    @Test
    void testDeleteBeer() throws Exception {
        var testBeer = beerServiceImpl.listBeers(null).get(0);

        given(beerService.deleteBeerById(any(UUID.class))).willReturn(Optional.of(testBeer));

        mockMvc.perform(delete(BeerController.BEER_PATH_ID,testBeer.getId())
                    .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());
        
        
        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(testBeer.getId());
    }

    @Test
    void testUpdateBeer() throws Exception {
        //
        var testBeer = beerServiceImpl.listBeers(null).get(0);

        given(beerService.updateExistingBeer(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(testBeer));

        mockMvc.perform(put(BeerController.BEER_PATH_ID,testBeer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testBeer)))
               .andExpect(status().isNoContent());

        verify(beerService).updateExistingBeer(eq(testBeer.getId()), eq(testBeer));
    }
    
    @Test
    void testUpdateBeerNameWithNull() throws Exception {
        //
        var testBeer = beerServiceImpl.listBeers(null).get(0);
        testBeer.setBeerName(null);

        given(beerService.updateExistingBeer(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(testBeer));

        MvcResult mvcresult = mockMvc.perform(put(BeerController.BEER_PATH_ID,testBeer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testBeer)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.length()", is(2))).andReturn();

        System.out.println(mvcresult.getResponse().getContentAsString());
        // verify(beerService).updateExistingBeer(eq(testBeer.getId()), eq(testBeer));
        verifyNoInteractions(beerService);
    }

    @Test
    void testCreateNewBeer() throws Exception {
        //

        var testBeer = beerServiceImpl.listBeers(null).get(0);
        testBeer.setVersion(null);
        testBeer.setId(null);

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null).get(1));

        mockMvc.perform(post(BeerController.BEER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testBeer)))
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"));
    }

    @Test
    void testCreateNewBeerWithNullBeerName() throws Exception {
        var testBeer = BeerDTO.builder().build();

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers(null).get(1));

        MvcResult mvcresult = mockMvc.perform(post(BeerController.BEER_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testBeer)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.length()", is(6))).andReturn();

        System.out.println(mvcresult.getResponse().getContentAsString());
    }


    @Test
    void listBeers() throws Exception {
        //
        var testListOfBeers = beerServiceImpl.listBeers(null);
        given(beerService.listBeers(null)).willReturn(testListOfBeers);

        mockMvc.perform(get(BeerController.BEER_PATH)
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.length()", is(3)));

    }


    @Test
    void getBeerById() throws Exception {

        var testBeer = beerServiceImpl.listBeers(null).get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));


        mockMvc.perform(get(BeerController.BEER_PATH_ID , testBeer.getId())
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
               .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName()))); 
    }

}
