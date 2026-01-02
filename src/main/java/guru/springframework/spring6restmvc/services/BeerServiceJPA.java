package guru.springframework.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

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
        var beerToSave = this.beerMapper.beerDtoToBeer(beer);
        beerToSave.setCreateDateTime(LocalDateTime.now());
        return this.beerMapper.beerToBeerDTO(this.beerRepository.save(beerToSave));
    }

    @Override
    public Optional<BeerDTO> updateExistingBeer(UUID id, BeerDTO entity) {

        AtomicReference<Optional<BeerDTO>> updatedBeer = new AtomicReference<>(Optional.empty());

        this.beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
            foundBeer.setBeerName(entity.getBeerName());
            foundBeer.setBeerStyle(entity.getBeerStyle());
            foundBeer.setPrice(entity.getPrice());
            foundBeer.setUpc(entity.getUpc());
            foundBeer.setQuantityOnHand(entity.getQuantityOnHand());
            foundBeer.setUpdateDateTime(LocalDateTime.now());
            updatedBeer.set(Optional.of(this.beerMapper.beerToBeerDTO(this.beerRepository.save(foundBeer))));
        }, () -> {
            updatedBeer.set(Optional.empty());
        });
        return updatedBeer.get();
    }

    @Override
    public void deleteBeerById(UUID beerId) {

    }

    @Override
    public void patchExistingBeer(UUID beerId, BeerDTO beer) {

    }
}
