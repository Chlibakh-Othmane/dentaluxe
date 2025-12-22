package ma.dentaluxe.mvc.dto.patient;

import lombok.Builder;
import lombok.Data;
import ma.dentaluxe.entities.enums.Assurance;
import ma.dentaluxe.entities.enums.Sexe;

import java.time.LocalDate;

@Data
@Builder
public class PatientCreateDTO {
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private Sexe sexe;
    private String adresse;
    private String telephone;
    private String email;
    private Assurance assurance;
}