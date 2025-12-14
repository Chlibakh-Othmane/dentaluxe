package ma.dentaluxe.mvc.dto.dossier;

import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;

public class AntecedentDTO {
    private Long idAntecedent;
    private Long idDM;
    private String nom;
    private CategorieAntecedent categorie;
    private NiveauRisque niveauDeRisque;

    public AntecedentDTO() {}

    public AntecedentDTO(Long idAntecedent, Long idDM, String nom, CategorieAntecedent categorie, NiveauRisque niveauDeRisque) {
        this.idAntecedent = idAntecedent;
        this.idDM = idDM;
        this.nom = nom;
        this.categorie = categorie;
        this.niveauDeRisque = niveauDeRisque;
    }

    // Getters et Setters
    public Long getIdAntecedent() { return idAntecedent; }
    public void setIdAntecedent(Long idAntecedent) { this.idAntecedent = idAntecedent; }
    public Long getIdDM() { return idDM; }
    public void setIdDM(Long idDM) { this.idDM = idDM; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public CategorieAntecedent getCategorie() { return categorie; }
    public void setCategorie(CategorieAntecedent categorie) { this.categorie = categorie; }
    public NiveauRisque getNiveauDeRisque() { return niveauDeRisque; }
    public void setNiveauDeRisque(NiveauRisque niveauDeRisque) { this.niveauDeRisque = niveauDeRisque; }
}