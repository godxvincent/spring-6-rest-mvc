package guru.springframework.spring6restmvc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.mappers.BeerMapper;
import guru.springframework.spring6restmvc.model.BeerDTO;
import guru.springframework.spring6restmvc.model.BeerStyle;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService
{

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    private final static Integer DEFAULT_PAGE_SIZE = 25;
    private final static Integer DEFAULT_PAGE_NUMBER = 0;


    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize) {

        Page<Beer> beerList;
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);


        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerList = findBeersByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerList = findBeersByBeerStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerList = findBeersByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else {
            beerList = this.beerRepository.findAll(pageRequest);
        }

        beerList.forEach(beer -> {
            if (showInventory != null && !showInventory)
            {
                beer.setQuantityOnHand(null) ;
            }
        });
        // return beerList.stream().map(beerMapper::beerToBeerDTO).toList();
        return beerList.map(beerMapper::beerToBeerDTO);
    }

    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {

        int queryPageNumber;
        int queryPageSize;

        if (pageNumber == null || pageNumber < 0) {
            queryPageNumber = DEFAULT_PAGE_NUMBER;
        } else {
            queryPageNumber = pageNumber - 1;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {

                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "beerName");

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }


    private Page<Beer> findBeersByBeerNameAndBeerStyle(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return this.beerRepository.findAllByBeerNameLikeIgnoreCaseAndBeerStyle("%"+beerName+"%", beerStyle, pageable);
    }

    private Page<Beer> findBeersByName(String beerName, Pageable pageable ) {
        return this.beerRepository.findAllByBeerNameLikeIgnoreCase("%" + beerName + "%", pageable);
    }

    private Page<Beer> findBeersByBeerStyle(BeerStyle beerStyle, Pageable pageable) {
        // return this.beerRepository.findAllByBeerNameLikeIgnoreCase("%" + beerName + "%");
        return this.beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(this.beerMapper.beerToBeerDTO(this.beerRepository.findById(id).orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        var beerToSave = this.beerMapper.beerDtoToBeer(beer);
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
            updatedBeer.set(Optional.of(this.beerMapper.beerToBeerDTO(this.beerRepository.save(foundBeer))));
        }, () -> {
            updatedBeer.set(Optional.empty());
        });
        return updatedBeer.get();
    }

    @Override
    public Optional<BeerDTO> deleteBeerById(UUID beerId) {
        AtomicReference<Optional<BeerDTO>> deletedBeer = new AtomicReference<>(Optional.empty());
        this.beerRepository.findById(beerId).ifPresentOrElse( (foundBeer) -> {
            deletedBeer.set(Optional.of(this.beerMapper.beerToBeerDTO(foundBeer)));
            this.beerRepository.delete(foundBeer);
        }, () -> {
            deletedBeer.set(Optional.empty());
        });
        return deletedBeer.get();
    }

    @Override
    public Optional<BeerDTO> patchExistingBeer(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> updatedBeer = new AtomicReference<>(Optional.empty());

        this.beerRepository.findById(beerId).ifPresentOrElse(existing -> {

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
            updatedBeer.set(Optional.of(this.beerMapper.beerToBeerDTO(this.beerRepository.save(existing))));
        }, () -> {
            updatedBeer.set(Optional.empty());
        });
        return updatedBeer.get();
    }
}
