package ma.dentaluxe.entities.consultation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterventionMedecin {
    private Long idIM;
    private Long idMedecin;
    private Long idConsultation;
    private Integer numDent;
    private Double prixIntervention;
}