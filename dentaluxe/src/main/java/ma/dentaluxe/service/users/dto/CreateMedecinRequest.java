package ma.dentaluxe.service.users.dto;

import ma.dentaluxe.entities.enums.Sexe;
import java.time.LocalDate;

public class CreateMedecinRequest {
    private String nom;
    private String prenom;
    private String email;
    private String tel;
    private String adresse;
    private String cin;
    private Sexe sexe;
    private String login;
    private String password;
    private LocalDate dateNaissance;

    // Champs spécifiques au médecin
    private String specialite;
    private String agendaMedecin;

    public CreateMedecinRequest() {}

    public CreateMedecinRequest(String nom, String prenom, String email, String tel,
                                String adresse, String cin, Sexe sexe, String login,
                                String password, LocalDate dateNaissance,
                                String specialite, String agendaMedecin) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
        this.adresse = adresse;
        this.cin = cin;
        this.sexe = sexe;
        this.login = login;
        this.password = password;
        this.dateNaissance = dateNaissance;
        this.specialite = specialite;
        this.agendaMedecin = agendaMedecin;
    }

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

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public String getAgendaMedecin() { return agendaMedecin; }
    public void setAgendaMedecin(String agendaMedecin) { this.agendaMedecin = agendaMedecin; }
}