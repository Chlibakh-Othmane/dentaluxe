package ma.dentaluxe.mvc.dto.patient;

import lombok.*;
import ma.dentaluxe.entities.enums.Assurance;
import ma.dentaluxe.entities.enums.Sexe;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientUpdateDTO {
    // On met les champs qu'on autorise à modifier
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    private LocalDate dateNaissance;
    private Sexe sexe;
    private Assurance assurance;
}