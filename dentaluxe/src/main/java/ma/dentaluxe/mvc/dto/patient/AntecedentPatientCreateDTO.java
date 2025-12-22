package ma.dentaluxe.mvc.dto.patient;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AntecedentPatientCreateDTO {
    private Long idAntecedent;
    private Long idPatient;
    private String notes;
    private boolean actif = true;
}