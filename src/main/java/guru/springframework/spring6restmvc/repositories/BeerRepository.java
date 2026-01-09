package guru.springframework.spring6restmvc.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;

public interface BeerRepository extends JpaRepository<Beer, UUID>{

    // Info about how to produce the methods "dynamically"
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
    List<Beer> findAllByBeerNameLikeIgnoreCase(String beerName);
    List<Beer> findAllByBeerStyle(BeerStyle beerStyle);


}
