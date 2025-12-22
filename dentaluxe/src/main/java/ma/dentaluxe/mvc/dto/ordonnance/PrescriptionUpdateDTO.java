package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrescriptionUpdateDTO {
    private Long idPrescription;
    private Integer quantite;
    private String frequence;
    private Integer dureeEnJours;
    private String posologie;
}