package ma.dentaluxe.mvc.dto.caisse;

import java.time.LocalDateTime;

public class ChargeDTO {
    private Long idCharge;
    private String titre;
    private String description;
    private Double montant;
    private LocalDateTime date;

    public ChargeDTO() {}

    public ChargeDTO(Long idCharge, String titre, String description, Double montant, LocalDateTime date) {
        this.idCharge = idCharge;
        this.titre = titre;
        this.description = description;
        this.montant = montant;
        this.date = date;
    }

    // Getters et Setters
    public Long getIdCharge() { return idCharge; }
    public void setIdCharge(Long idCharge) { this.idCharge = idCharge; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}