package ma.dentaluxe.entities.dossier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Antecedents {
    private Long idAntecedent;
    private Long idDM;
    private String nom;
    private CategorieAntecedent categorie;
    private NiveauRisque niveauDeRisque;
}