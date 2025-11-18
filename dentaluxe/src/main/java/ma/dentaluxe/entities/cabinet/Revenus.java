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
public class Revenus {
    private Long idRevenu;
    private Long idCabinet;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime dateRevenu;
}