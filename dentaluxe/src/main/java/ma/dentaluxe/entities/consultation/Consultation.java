package ma.dentaluxe.entities.consultation;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.StatutConsultation;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Consultation {
    private Long idConsultation;
    private Long idRDV;
    private Long idDM;
    private Long idMedecin;
    private LocalDate dateConsultation;
    private StatutConsultation statut;
    private String observation;
}