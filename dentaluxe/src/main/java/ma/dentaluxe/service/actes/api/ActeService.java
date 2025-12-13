// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.actes.api;

import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;

import java.util.List;

public interface ActeService {

    // ========== CRUD de base ==========
    void createActe(Acte acte);
    Acte getActeById(Long id);
    List<Acte> getAllActes();

    void addActe(Acte acte);

    void updateActe(Acte acte);
    void deleteActe(Long id);

    void deleteActeById(Long id);

    // ========== Recherche et filtres ==========
    List<Acte> getActesByCategorie(CategorieActe categorie);
    List<Acte> searchActesByLibelle(String keyword);
    Acte getActeByLibelle(String libelle);

    // ========== Statistiques ==========
    int countActes();
    int countActesByCategorie(CategorieActe categorie);
    double getAveragePrixActes();
    Acte getMostExpensiveActe();
    Acte getCheapestActe();
    double getTotalPrixActes();

    // ========== Validation métier ==========
    boolean validateActe(Acte acte);
    boolean acteExists(Long id);
    boolean acteExistsByLibelle(String libelle);

    // ========== Gestion des prix ==========
    void updatePrix(Long idActe, double nouveauPrix);
    void applyDiscount(Long idActe, double pourcentage);
    List<Acte> getActesByPriceRange(double prixMin, double prixMax);
}