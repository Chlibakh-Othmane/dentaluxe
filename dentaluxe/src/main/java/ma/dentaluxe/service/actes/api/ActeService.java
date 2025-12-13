// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.actes.api;
import ma.dentaluxe.mvc.dto.ActeDTO;
import ma.dentaluxe.entities.enums.CategorieActe;

import java.util.List;

/**
 * Service pour la gestion des actes dentaires
 * Toutes les méthodes travaillent avec ActeDTO
 */
public interface ActeService {

    // ========== CRUD de base ==========

    ActeDTO createActe(ActeDTO acteDTO);

    ActeDTO getActeById(Long id);

    List<ActeDTO> getAllActes();

    ActeDTO updateActe(ActeDTO acteDTO);

    void deleteActe(Long id);

    // ========== Recherche et filtres ==========


    List<ActeDTO> getActesByCategorie(CategorieActe categorie);


    List<ActeDTO> searchActesByLibelle(String keyword);

    ActeDTO getActeByLibelle(String libelle);

    List<ActeDTO> getActesByPriceRange(double prixMin, double prixMax);

    List<ActeDTO> getActesSortedByPrice(boolean ascending);

    List<ActeDTO> getActesSortedByLibelle();

    // ========== Statistiques ==========
    int countActes();

    int countActesByCategorie(CategorieActe categorie);

    double getAveragePrixActes();

    ActeDTO getMostExpensiveActe();

    ActeDTO getCheapestActe();

    double getTotalPrixActes();

    List<CategorieStatDTO> getStatistiquesByCategorie();

    // ========== Validation métier ==========

    boolean validateActe(ActeDTO acteDTO);

    boolean acteExists(Long id);

    boolean acteExistsByLibelle(String libelle);

    // ========== Gestion des prix ==========

    ActeDTO updatePrix(Long idActe, double nouveauPrix);

    ActeDTO applyDiscount(Long idActe, double pourcentage);

    List<ActeDTO> applyDiscountToCategorie(CategorieActe categorie, double pourcentage);

    double calculatePrixAvecRemise(Long idActe, double pourcentage);

    // ========== Classe interne pour les statistiques ==========

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    class CategorieStatDTO {
        private CategorieActe categorie;
        private int nombreActes;
        private double prixMoyen;
        private double prixTotal;
        private double prixMin;
        private double prixMax;
    }
}