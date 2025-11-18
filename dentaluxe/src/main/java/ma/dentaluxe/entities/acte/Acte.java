package ma.dentaluxe.entities.acte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.CategorieActe;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Acte {
    private Long idActe;
    private Long idInterventionMedecin;
    private String libelle;
    private String description;
    private Double prixDeBase;
    private CategorieActe categorie;
}
