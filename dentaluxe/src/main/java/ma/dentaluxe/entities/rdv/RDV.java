package ma.dentaluxe.entities.rdv;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.StatutRDV;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RDV {
    private Long idRDV;
    private Long idDM;
    private Long idMedecin;
    private LocalDate dateRDV;
    private LocalTime heureRDV;
    private String motif;
    private StatutRDV statut;
    private String noteMedecin;
}