package ma.dentaluxe.entities.patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.Assurance;
import ma.dentaluxe.entities.enums.Sexe;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
//@Builder: crrer un patients avec les infos que je veux
//@NoArgsConstructor:pour apeler le constructeur par defaut
//@AllArgsConstructor:pour apeler le constructeur avec tous les attributs
//@Data:pour apeler les  getters et setters...



//les annotation @ (pourquoi ne sont pas la )
public class Patient {



    private Long id;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    private LocalDate dateNaissance;
    private LocalDateTime dateCreation;
    private Sexe sexe;
    private Assurance assurance;


}
