package guru.springframework.spring6restmvc.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;

public interface BeerService {
    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO saveNewBeer(BeerDTO beer);
    Optional<BeerDTO> updateExistingBeer(UUID id, BeerDTO entity);
    Optional<BeerDTO> deleteBeerById(UUID beerId);
    Optional<BeerDTO> patchExistingBeer(UUID beerId, BeerDTO beer);
}
