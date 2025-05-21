package guru.springframework.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.model.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

    private HashMap<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        super();
        
        beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
                .id(UUID.randomUUID())
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal(12.50))
                .quantityOnHand(120)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
        Beer beer2 = Beer.builder()
                .id(UUID.randomUUID())
                .beerName("Modelo")
                .beerStyle(BeerStyle.LAGER)
                .upc("498724")
                .price(new BigDecimal(5.99))
                .quantityOnHand(200)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
        Beer beer3 = Beer.builder()
                .id(UUID.randomUUID())
                .beerName("Heineken")
                .beerStyle(BeerStyle.STOUT)
                .upc("987123")
                .price(new BigDecimal(8.31))
                .quantityOnHand(500)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(), beer1);
        beerMap.put(beer2.getId(), beer2);
        beerMap.put(beer3.getId(), beer3);

    }

    @Override
    public List<Beer> listBeers() {
        return new ArrayList<Beer>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {

        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return beerMap.get(id);

    }


}
