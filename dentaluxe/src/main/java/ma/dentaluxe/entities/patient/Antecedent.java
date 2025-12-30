package ma.dentaluxe.entities.patient;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import ma.dentaluxe.entities.base.BaseEntity;
import ma.dentaluxe.entities.enums.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Antecedent extends BaseEntity {
    private Long idAntecedent;
    private Long idDM; // Changé en Long (objet)
    private String nom;
    private CategorieAntecedent categorie;
    private NiveauRisque niveauRisque;

    @Builder.Default
    private List<Patient> patients = new ArrayList<>();

    // RECTIFICATION DU SETTER : Il doit enregistrer la valeur !
    public void setIdAntecedent(Long id) {
        this.idAntecedent = id;
    }

    // Pour la compatibilité avec ton test qui utilise getId()
    public Long getId() {
        return idAntecedent;
    }
}