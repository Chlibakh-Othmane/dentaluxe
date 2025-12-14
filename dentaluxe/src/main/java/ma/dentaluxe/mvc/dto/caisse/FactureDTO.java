package ma.dentaluxe.mvc.dto.caisse;

import ma.dentaluxe.entities.enums.StatutFacture;
import java.time.LocalDateTime;

public class FactureDTO {
    private Long idFacture;
    private Long idSF;
    private Long idConsultation;
    private Double totalFacture;
    private Double totalDesActes; // <--- AJOUTÉ
    private Double montantPaye;
    private Double reste;
    private StatutFacture statut;
    private LocalDateTime dateCreation;

    public FactureDTO() {}

    // Constructeur complet mis à jour
    public FactureDTO(Long idFacture, Long idSF, Long idConsultation, Double totalFacture, Double totalDesActes, Double montantPaye, Double reste, StatutFacture statut, LocalDateTime dateCreation) {
        this.idFacture = idFacture;
        this.idSF = idSF;
        this.idConsultation = idConsultation;
        this.totalFacture = totalFacture;
        this.totalDesActes = totalDesActes; // <--- AJOUTÉ
        this.montantPaye = montantPaye;
        this.reste = reste;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    // --- GETTERS ET SETTERS ---

    public Long getIdFacture() { return idFacture; }
    public void setIdFacture(Long idFacture) { this.idFacture = idFacture; }

    public Long getIdSF() { return idSF; }
    public void setIdSF(Long idSF) { this.idSF = idSF; }

    public Long getIdConsultation() { return idConsultation; }
    public void setIdConsultation(Long idConsultation) { this.idConsultation = idConsultation; }

    public Double getTotalFacture() { return totalFacture; }
    public void setTotalFacture(Double totalFacture) { this.totalFacture = totalFacture; }

    // 👇 VOICI LES MÉTHODES QUI MANQUAIENT 👇
    public Double getTotalDesActes() { return totalDesActes; }
    public void setTotalDesActes(Double totalDesActes) { this.totalDesActes = totalDesActes; }
    // ----------------------------------------

    public Double getMontantPaye() { return montantPaye; }
    public void setMontantPaye(Double montantPaye) { this.montantPaye = montantPaye; }

    public Double getReste() { return reste; }
    public void setReste(Double reste) { this.reste = reste; }

    public StatutFacture getStatut() { return statut; }
    public void setStatut(StatutFacture statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}