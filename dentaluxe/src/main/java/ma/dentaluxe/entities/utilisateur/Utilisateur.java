package ma.dentaluxe.entities.utilisateur;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.dentaluxe.entities.enums.Sexe;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Utilisateur {
    private Long id;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private String createdBy;
    private String updatedBy;
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String adresse;
    private String cin;
    private Sexe sexe;
    private String login;
    private String passwordHash;
    private LocalDateTime lastLoginDate;
    private LocalDate dateNaissance;
    private Boolean actif;
}