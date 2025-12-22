package ma.dentaluxe.mvc.dto.medicament;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MedicamentDTO {
    private Long idMedicament;
    private String nom;
    private String type;
    private String forme;
    private String description ;
    private Double prixUnitaire;
    private Boolean remboursable;
}