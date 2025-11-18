package ma.dentaluxe.entities.ordonnance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prescription {
    private Long idPrescription;
    private Long idOrdo;
    private Long idMedicament;
    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;
}