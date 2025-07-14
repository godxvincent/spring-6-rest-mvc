package guru.springframework.spring6restmvc.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Customer {
    @Id
    private UUID id;
    @Version
    private Integer version;
    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
