package ma.dentaluxe.entities.ordonnance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medicament {
    private Long idMedicament;
    private String nom;
    private String type;
    private String forme;
    private Boolean remboursable;
    private Double prixUnitaire;
    private String description;
}