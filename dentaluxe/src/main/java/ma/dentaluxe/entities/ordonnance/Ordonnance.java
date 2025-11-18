package ma.dentaluxe.entities.ordonnance;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ordonnance {
    private Long idOrdo;
    private Long idDM;
    private Long idMedecin;
    private LocalDate dateOrdonnance;
}