package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import guru.springframework.spring6restmvc.model.BeerDTO;

public interface BeerService {
    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO saveNewBeer(BeerDTO beer);
    Optional<BeerDTO> updateExistingBeer(UUID id, BeerDTO entity);
    void deleteBeerById(UUID beerId);
    void patchExistingBeer(UUID beerId, BeerDTO beer);
}
