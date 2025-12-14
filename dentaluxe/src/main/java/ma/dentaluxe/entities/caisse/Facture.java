package ma.dentaluxe.entities.caisse;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.StatutFacture;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Facture {
    private Long idFacture;
    private Long idSF;
    private Long idConsultation;
    private Double totalFacture;
    private Double totalDesActes;
    private Double montantPaye;
    private Double reste;
    private StatutFacture statut;
    private LocalDateTime dateCreation;
}