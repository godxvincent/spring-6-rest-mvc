package guru.springframework.spring6restmvc.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import guru.springframework.spring6restmvc.model.BeerStyle;
import java.math.BigDecimal;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Beer {
    @Id
    @GeneratedValue(generator = "UUID")
    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator") // Deprecated, instead use UuidGenerator
    @UuidGenerator
    // Cambiamos columnDefinition = "varchar" por columnDefinition = "varchar(36)" ya que hibernate estaba usando ese valor para crear la columna en mysql lo cual no es soportado.
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    // Añadimos esto ya que hibernate esta tratando de salvar una columna binaria en un string
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID id;
    @Version
    private Integer version;

    @NotNull
    @NotBlank
    @Size(max = 50)
    // @Column(length = 50)
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
