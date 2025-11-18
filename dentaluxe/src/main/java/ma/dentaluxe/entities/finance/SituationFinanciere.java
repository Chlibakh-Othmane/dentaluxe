package ma.dentaluxe.entities.finance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.StatutSituationFinanciere;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SituationFinanciere {
    private Long idSF;
    private Long idDM;
    private Double totalDesActes;
    private Double totalPaye;
    private Double resteDu;
    private Double creance;
    private StatutSituationFinanciere statut;
    private Boolean enPromo;
}