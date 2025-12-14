package ma.dentaluxe.mvc.dto.dossier;

import ma.dentaluxe.entities.enums.StatutConsultation;
import java.time.LocalDate;

public class ConsultationDTO {
    private Long idConsultation;
    private Long idDM;
    private Long idRDV;
    private Long idMedecin;
    private LocalDate dateConsultation;
    private String motif;
    private StatutConsultation statut;
    private String observation;

    public ConsultationDTO() {}

    public ConsultationDTO(Long idConsultation, Long idDM, Long idRDV, Long idMedecin, LocalDate dateConsultation, String motif, StatutConsultation statut, String observation) {
        this.idConsultation = idConsultation;
        this.idDM = idDM;
        this.idRDV = idRDV;
        this.idMedecin = idMedecin;
        this.dateConsultation = dateConsultation;
        this.motif = motif;
        this.statut = statut;
        this.observation = observation;
    }

    // Getters et Setters
    public Long getIdConsultation() { return idConsultation; }
    public void setIdConsultation(Long idConsultation) { this.idConsultation = idConsultation; }
    public Long getIdDM() { return idDM; }
    public void setIdDM(Long idDM) { this.idDM = idDM; }
    public Long getIdRDV() { return idRDV; }
    public void setIdRDV(Long idRDV) { this.idRDV = idRDV; }
    public Long getIdMedecin() { return idMedecin; }
    public void setIdMedecin(Long idMedecin) { this.idMedecin = idMedecin; }
    public LocalDate getDateConsultation() { return dateConsultation; }
    public void setDateConsultation(LocalDate dateConsultation) { this.dateConsultation = dateConsultation; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public StatutConsultation getStatut() { return statut; }
    public void setStatut(StatutConsultation statut) { this.statut = statut; }
    public String getObservation() { return observation; }
    public void setObservation(String observation) { this.observation = observation; }
}