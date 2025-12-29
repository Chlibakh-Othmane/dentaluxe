package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrescriptionCreateDTO {
    private Long idOrdo;
    private Long idMedicament;
    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;
    private String posologie;
}