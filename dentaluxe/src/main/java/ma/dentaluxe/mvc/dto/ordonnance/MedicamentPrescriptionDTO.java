package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MedicamentPrescriptionDTO {
    private Long idMedicament;
    private String nomMedicament;
    private int nombrePrescriptions;
    private int quantiteTotalePrescrite;
    private String frequenceMoyenne;
}