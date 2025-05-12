package guru.springframework.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

    @Override
    public Beer getBeerById(UUID id) {

        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        // TODO Auto-generated method stub
        return Beer.builder()
                .id(id)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal(12.50))
                .quantityOnHand(120)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();

    }


}
