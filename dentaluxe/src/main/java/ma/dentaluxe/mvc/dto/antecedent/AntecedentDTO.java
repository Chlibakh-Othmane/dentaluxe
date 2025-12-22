package ma.dentaluxe.mvc.dto.antecedent;

import lombok.*;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AntecedentDTO {
    private Long id;
    private Long idDM; // ID du dossier médical lié
    private String nom;
    private CategorieAntecedent categorie;
    private NiveauRisque niveauRisque;
}