package ma.dentaluxe.entities.dossier;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder


public class DossierMedical {
    private Long idDM;
    private Long idPatient;
    private LocalDate dateDeCreation;
}