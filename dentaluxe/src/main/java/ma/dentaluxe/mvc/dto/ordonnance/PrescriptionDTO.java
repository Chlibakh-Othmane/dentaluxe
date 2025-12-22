package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrescriptionDTO {
    private Long idPrescription;
    private Long idMedicament;
    private String nomMedicament; // Très utile pour l'affichage Swing sans refaire d'appel SQL
    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;
}