package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;
import java.util.List;
import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrescriptionReportDTO {
    private Long idOrdo;
    private int nombrePrescriptions;
    private int dureeMoyenneTraitement;
    private List<String> categoriesMedicaments;
    private Map<String, Integer> repartitionParFrequence;
    private List<PrescriptionDTO> prescriptions;
}