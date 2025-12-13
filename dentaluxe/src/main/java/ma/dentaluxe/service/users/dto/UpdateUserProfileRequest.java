package ma.dentaluxe.service.users.dto;

import ma.dentaluxe.entities.enums.Sexe;
import java.time.LocalDate;

public class UpdateUserProfileRequest {
    private String nom;
    private String prenom;
    private String tel;
    private String adresse;
    private Sexe sexe;
    private LocalDate dateNaissance;

    public UpdateUserProfileRequest() {}

    public UpdateUserProfileRequest(String nom, String prenom, String tel,
                                    String adresse, Sexe sexe, LocalDate dateNaissance) {
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.adresse = adresse;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
    }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public Sexe getSexe() { return sexe; }
    public void setSexe(Sexe sexe) { this.sexe = sexe; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }
}