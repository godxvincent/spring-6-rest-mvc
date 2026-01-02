package guru.springframework.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class BeerServiceImpl implements BeerService {

    private HashMap<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        super();
        
        beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Galaxy Cat")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("12356")
                .price(new BigDecimal(12.50))
                .quantityOnHand(120)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
        BeerDTO beer2 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Modelo")
                .beerStyle(BeerStyle.LAGER)
                .upc("498724")
                .price(new BigDecimal(5.99))
                .quantityOnHand(200)
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
        BeerDTO beer3 = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
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
    public List<BeerDTO> listBeers() {
        return new ArrayList<BeerDTO>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.debug("Get Beer by Id - in service. Id: " + id.toString());
        return Optional.of(beerMap.get(id));

    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        // TODO Auto-generated method stub
        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(beer.getVersion())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .createDateTime(LocalDateTime.now())
                .updateDateTime(LocalDateTime.now())
                .build();
        beerMap.put(savedBeer.getId(), savedBeer);
        return savedBeer;
    }

    @Override
    public Optional<BeerDTO> updateExistingBeer(UUID id, BeerDTO beer) {
        BeerDTO existing = beerMap.get(id);
        existing.setBeerName(beer.getBeerName());
        existing.setBeerStyle(beer.getBeerStyle());
        existing.setPrice(beer.getPrice());
        existing.setUpc(beer.getUpc());
        existing.setUpdateDateTime(LocalDateTime.now());
        existing.setQuantityOnHand(beer.getQuantityOnHand());
        beerMap.put(existing.getId(), existing);
        return Optional.of(existing);
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerMap.remove(beerId);

        
    }

    @Override
    public void patchExistingBeer(UUID beerId, BeerDTO beer) {
        var existing = beerMap.get(beerId);
        if (existing != null) {
            if (StringUtils.hasText(beer.getBeerName())) {
                existing.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null) {
                existing.setBeerStyle(beer.getBeerStyle());
            }
            if (beer.getPrice() != null) {
                existing.setPrice(beer.getPrice());
            } 
            if (StringUtils.hasText(beer.getUpc())) {
                existing.setUpc(beer.getUpc());
            }
            if (beer.getQuantityOnHand() != null) {
                existing.setQuantityOnHand(beer.getQuantityOnHand());
            }
            existing.setUpdateDateTime(LocalDateTime.now());
            beerMap.put(existing.getId(), existing);
        }
    }


}
