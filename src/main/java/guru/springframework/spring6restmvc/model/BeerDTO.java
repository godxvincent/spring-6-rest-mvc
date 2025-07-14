package guru.springframework.spring6restmvc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import guru.springframework.spring6restmvc.model.BeerStyle;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;
    private String beerName;
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private BigDecimal price;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
