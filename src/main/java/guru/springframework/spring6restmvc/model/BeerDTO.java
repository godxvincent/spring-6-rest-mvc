package guru.springframework.spring6restmvc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;
    
    @NotNull
    @NotBlank
    private String beerName;

    @NotNull
    private BeerStyle beerStyle;

    @NotNull
    @NotBlank
    private String upc;

    private Integer quantityOnHand;

    @NotNull
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal price;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;
}
