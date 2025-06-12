package guru.springframework.spring6restmvc.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import guru.springframework.spring6restmvc.services.BeerService;
import guru.springframework.spring6restmvc.services.BeerServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;

import static org.mockito.BDDMockito.given;


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

    // Incluimos aqui el servicio real ya que esta retornando data mock de cualquier forma
    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();

    @Test
    void listBeers() throws Exception {
        //
        var testListOfBeers = beerServiceImpl.listBeers();
        given(beerService.listBeers()).willReturn(testListOfBeers);

        mockMvc.perform(get("/api/v1/beer")
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.length()", is(3)));

    }


    @Test
    void getBeerById() throws Exception {
        // System.out.println("Beer Controller Test");
        // System.out.println(beerController.getBeerById(UUID.randomUUID()));
        var testBeer = beerServiceImpl.listBeers().get(0);

        given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);


        mockMvc.perform(get("/api/v1/beer/" + testBeer.getId())
               .accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
               .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName()))); 
    }

}
