package ma.dentaluxe.entities.patient;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.base.BaseEntity;
import ma.dentaluxe.entities.enums.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Antecedent extends BaseEntity {
    private String nom;
    private CategorieAntecedent categorie;
    private NiveauRisque niveauRisque;

    private List<Patient> patients = new ArrayList<>();

    @Override
    public String toString() {
        return """
        Antecedent {
          id = %d,
          nom = '%s',
          categorie = %s,
          niveauRisque = %s,
          patientsCount = %d
        }
        """.formatted(
                id,
                nom,
                categorie,
                niveauRisque,
                patients == null ? 0 : patients.size()
        );
    }

    public void setIdAntecedent(long aLong) {
    }

    private Long idDM;

    public Long getIdDM() {
        return idDM;
    }

}

