package ma.dentaluxe.service.users.dto;

import ma.dentaluxe.entities.enums.Sexe;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserAccountDto {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String adresse;
    private String cin;
    private Sexe sexe;
    private String login;
    private LocalDate dateNaissance;
    private Boolean actif;
    private LocalDateTime creationDate;
    private LocalDateTime lastLoginDate;

    // Rôle de l'utilisateur
    private String role; // "ADMIN", "MEDECIN", "SECRETAIRE"

    // Champs spécifiques au médecin
    private String specialite;
    private String agendaMedecin;

    // Champs spécifiques au secrétaire
    private String numCnss;
    private BigDecimal commission;

    public UserAccountDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public Sexe getSexe() { return sexe; }
    public void setSexe(Sexe sexe) { this.sexe = sexe; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public LocalDateTime getLastLoginDate() { return lastLoginDate; }
    public void setLastLoginDate(LocalDateTime lastLoginDate) { this.lastLoginDate = lastLoginDate; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getAgendaMedecin() { return agendaMedecin; }
    public void setAgendaMedecin(String agendaMedecin) { this.agendaMedecin = agendaMedecin; }

    public String getNumCnss() { return numCnss; }
    public void setNumCnss(String numCnss) { this.numCnss = numCnss; }

    public BigDecimal getCommission() { return commission; }
    public void setCommission(BigDecimal commission) { this.commission = commission; }

    public String getFullName() {
        return prenom + " " + nom;
    }
}