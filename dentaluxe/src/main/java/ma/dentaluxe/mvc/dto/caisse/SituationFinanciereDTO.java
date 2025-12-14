package ma.dentaluxe.mvc.dto.caisse;

import ma.dentaluxe.entities.enums.StatutSituationFinanciere;

public class SituationFinanciereDTO {
    private Long idSF;
    private Long idDM;
    private Double totalGlobal;
    private Double totalPaye;
    private Double resteDu;
    private StatutSituationFinanciere statut;

    public SituationFinanciereDTO() {}

    public SituationFinanciereDTO(Long idSF, Long idDM, Double totalGlobal, Double totalPaye, Double resteDu, StatutSituationFinanciere statut) {
        this.idSF = idSF;
        this.idDM = idDM;
        this.totalGlobal = totalGlobal;
        this.totalPaye = totalPaye;
        this.resteDu = resteDu;
        this.statut = statut;
    }

    // Getters et Setters
    public Long getIdSF() { return idSF; }
    public void setIdSF(Long idSF) { this.idSF = idSF; }
    public Long getIdDM() { return idDM; }
    public void setIdDM(Long idDM) { this.idDM = idDM; }
    public Double getTotalGlobal() { return totalGlobal; }
    public void setTotalGlobal(Double totalGlobal) { this.totalGlobal = totalGlobal; }
    public Double getTotalPaye() { return totalPaye; }
    public void setTotalPaye(Double totalPaye) { this.totalPaye = totalPaye; }
    public Double getResteDu() { return resteDu; }
    public void setResteDu(Double resteDu) { this.resteDu = resteDu; }
    public StatutSituationFinanciere getStatut() { return statut; }
    public void setStatut(StatutSituationFinanciere statut) { this.statut = statut; }
}