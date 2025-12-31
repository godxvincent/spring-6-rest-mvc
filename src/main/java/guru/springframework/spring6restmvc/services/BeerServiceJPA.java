package guru.springframework.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService
{

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;


    @Override
    public List<BeerDTO> listBeers() {
        return this.beerRepository.findAll().stream().map(beerMapper::beerToBeerDTO).toList();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(this.beerMapper.beerToBeerDTO(this.beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        return null;
    }

    @Override
    public void updateExistingBeer(UUID id, BeerDTO entity) {

    }

    @Override
    public void deleteBeerById(UUID beerId) {

    }

    @Override
    public void patchExistingBeer(UUID beerId, BeerDTO beer) {

    }
}
