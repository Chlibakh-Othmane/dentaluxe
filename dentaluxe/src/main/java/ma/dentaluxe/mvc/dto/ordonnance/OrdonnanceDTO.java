package ma.dentaluxe.mvc.dto.ordonnance;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdonnanceDTO {
    private Long idOrdo;
    private Long idDM;
    private Long idMedecin;
    private String nomPatient; // Pour l'en-tête de l'ordonnance
    private String nomMedecin;
    private LocalDate dateOrdonnance;
    private List<PrescriptionDTO> prescriptions; // La liste des médicaments prescrits
}