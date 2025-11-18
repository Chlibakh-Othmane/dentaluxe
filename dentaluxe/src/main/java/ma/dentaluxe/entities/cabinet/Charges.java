package ma.dentaluxe.entities.cabinet;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Charges {
    private Long idCharge;
    private Long idCabinet;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime dateCharge;
}