package ma.dentaluxe.mvc.dto.patient;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AntecedentPatientDTO {
    private Long idAntecedent;
    private Long idPatient;

    // Informations venant de la table de liaison
    private LocalDate dateAjout;
    private boolean actif;
    private String notes;

    // Informations jointes (venant de la table Antecedents) pour l'affichage Swing
    private String nomAntecedent;
    private String categorie;
    private String niveauRisque;

    // Méthode utilitaire pour Swing
    public String getStatusDescription() {
        return actif ? "Actif" : "Historique (Inactif)";
    }
}