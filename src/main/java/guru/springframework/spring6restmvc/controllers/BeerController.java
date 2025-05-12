package guru.springframework.spring6restmvc.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("/beers")
@Slf4j
@Controller
@AllArgsConstructor
public class BeerController {

    private final BeerService beerService;


    public Beer getBeerById(UUID id){

        log.debug("Get Beer by Id - in controller");

        return beerService.getBeerById(id);
    }

}
