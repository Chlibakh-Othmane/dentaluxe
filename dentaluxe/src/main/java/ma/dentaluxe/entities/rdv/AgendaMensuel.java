package ma.dentaluxe.entities.rdv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendaMensuel {
    private Long idAgenda;
    private Long idMedecin;
    private String mois;
    private Integer annee;
    private String joursNonDisponible;
}