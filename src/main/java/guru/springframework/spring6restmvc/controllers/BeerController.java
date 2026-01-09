package guru.springframework.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@AllArgsConstructor
@RestController
// @RequestMapping("/api/v1/beer")
public class BeerController {


    public final static String BEER_PATH = "/api/v1/beer";
    public final static String BEER_PATH_ID = BEER_PATH + "/{beerId}";


    private final BeerService beerService;


    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity patchBeer(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beer){

        if (beerService.patchExistingBeer(beerId, beer).isEmpty()) {
            throw new NotFoundException();
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteBeer(@PathVariable("beerId") UUID beerId){

        if (beerService.deleteBeerById(beerId).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<BeerDTO> putBeer(@PathVariable("beerId") UUID id, @Validated @RequestBody BeerDTO entity) {
        
        if (beerService.updateExistingBeer(id, entity).isEmpty()) {
            throw new NotFoundException();
        }
        return new ResponseEntity<BeerDTO>(HttpStatus.NO_CONTENT);
    }

    // @validated forces the service to apply and validate the DTO follows any validation before the call.
    @PostMapping(BEER_PATH)
    public ResponseEntity<BeerDTO> createBeer(@Validated @RequestBody BeerDTO beer){
        BeerDTO savedBeer = beerService.saveNewBeer(beer);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/beer/" + savedBeer.getId().toString());

        // return ResponseEntity.ok(savedBeer);
        return new ResponseEntity<BeerDTO>(headers, HttpStatus.CREATED);
    }


    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers(
                        @RequestParam(required = false) String beerName, 
                        @RequestParam(required = false) BeerStyle beerStyle,
                        @RequestParam(required = false) Boolean showInventory
                    ) {
        return beerService.listBeers(beerName, beerStyle, showInventory);
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId){

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    // This is a first approach to handle the exceptions when it's required for an specific necessity
    // We can implement the function exception handler or we can create a kind of base controller.
    // @ExceptionHandler(NotFoundException.class)
    // public ResponseEntity handleNotFoundException(){
    //     return new ResponseEntity(HttpStatus.NOT_FOUND).notFound().build();
    // }
}
