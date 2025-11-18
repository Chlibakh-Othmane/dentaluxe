package ma.dentaluxe.entities.certificat;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Certificat {
    private Long idCertif;
    private Long idDM;
    private Long idMedecin;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer duree;
    private String noteMedecin;
}