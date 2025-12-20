package ma.dentaluxe.entities.AntecedentPatient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AntecedentPatient {
    private Long idAntecedent;
    private Long idPatient;
    private LocalDate dateAjout;
    private boolean actif;
    private String notes;

}
