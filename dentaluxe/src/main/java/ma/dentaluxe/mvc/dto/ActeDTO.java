package ma.dentaluxe.mvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.CategorieActe;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActeDTO {

    private Long idActe;
    private Long idInterventionMedecin;
    private String libelle;
    private String description;
    private Double prixDeBase;
    private CategorieActe categorie;

    // Champs calculés/supplémentaires pour l'affichage
    private String categorieLibelle;
    private Double prixAvecRemise;
    private String interventionDetails;
    private Integer nombreUtilisations;
}