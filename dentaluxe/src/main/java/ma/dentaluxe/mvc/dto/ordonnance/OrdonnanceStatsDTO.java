package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdonnanceStatsDTO {
    private int totalOrdonnances;
    private int ordonnancesCeMois;
    private double moyenneMedicamentsParOrdonnance;
    private String medicamentLePlusPrescrit;
    private String medecinLePlusActif;
    private Map<String, Integer> repartitionParMois;
}