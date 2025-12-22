package ma.dentaluxe.mvc.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.Assurance;
import ma.dentaluxe.entities.enums.Sexe;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data                // Génère Getters, Setters, toString, equals, hashCode
@Builder             // Permet de faire PatientDTO.builder()...build()
@NoArgsConstructor   // Constructeur vide (nécessaire pour certaines technos)
@AllArgsConstructor  // Constructeur avec tous les champs
public class PatientDTO {

    // Informations de base
    private Long id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private Sexe sexe;

    // Coordonnées
    private String adresse;
    private String telephone;
    private String email;

    // Informations administratives
    private Assurance assurance;
    private LocalDateTime dateCreation;

    // --- CHAMPS CALCULÉS (Très utiles pour Swing) ---

    private Integer age; // Calculé par le service (Period.between...)

    /**
     * Retourne le nom complet pour l'afficher dans les titres ou les labels.
     */
    public String getNomComplet() {
        return (prenom != null ? prenom : "") + " " + (nom != null ? nom.toUpperCase() : "");
    }

    /**
     * Utile pour afficher une ligne résumée dans une JList ou un tableau Swing.
     */
    public String getResume() {
        return getNomComplet() + " (" + (telephone != null ? telephone : "Pas de tel") + ")";
    }
    public String getDateCreationFormatee() {
        if (this.dateCreation == null) {
            return "N/A";
        }
        // On transforme le LocalDateTime en String via ton utilitaire
        // Note : dateCreation.toLocalDate() car DateUtils attend un LocalDate
        return ma.dentaluxe.common.utilitaire.DateUtils.formatToFrench(this.dateCreation.toLocalDate());
    }
}