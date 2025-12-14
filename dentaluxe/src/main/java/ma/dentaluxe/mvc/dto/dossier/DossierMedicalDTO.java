package ma.dentaluxe.mvc.dto.dossier;

import java.time.LocalDate;

public class DossierMedicalDTO {
    private Long idDM;
    private Long idPatient;
    private LocalDate dateDeCreation;

    public DossierMedicalDTO() {} // Constructeur vide obligatoire

    public DossierMedicalDTO(Long idDM, Long idPatient, LocalDate dateDeCreation) {
        this.idDM = idDM;
        this.idPatient = idPatient;
        this.dateDeCreation = dateDeCreation;
    }

    // Getters et Setters
    public Long getIdDM() { return idDM; }
    public void setIdDM(Long idDM) { this.idDM = idDM; }
    public Long getIdPatient() { return idPatient; }
    public void setIdPatient(Long idPatient) { this.idPatient = idPatient; }
    public LocalDate getDateDeCreation() { return dateDeCreation; }
    public void setDateDeCreation(LocalDate dateDeCreation) { this.dateDeCreation = dateDeCreation; }
}