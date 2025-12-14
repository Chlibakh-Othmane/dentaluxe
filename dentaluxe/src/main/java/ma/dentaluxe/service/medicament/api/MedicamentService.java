package ma.dentaluxe.service.medicament.api;

import ma.dentaluxe.entities.ordonnance.Medicament;
import java.util.List;

/**
 * Service pour la gestion des médicaments.
 *
 * @author CHLIBAKH OTHMANE
 */
public interface MedicamentService {

    // ==================== CRUD MÉDICAMENTS ====================

    /**
     * Récupère tous les médicaments
     */
    List<Medicament> findAll();

    /**
     * Récupère un médicament par son ID
     */
    Medicament findById(Long id);

    /**
     * Crée un nouveau médicament
     */
    Medicament create(Medicament medicament);

    /**
     * Met à jour un médicament existant
     */
    Medicament update(Long id, Medicament medicament);

    /**
     * Supprime un médicament
     */
    void delete(Long id);

    // ==================== RECHERCHE ET FILTRES ====================

    /**
     * Recherche des médicaments par nom (recherche partielle)
     */
    List<Medicament> findByNom(String nom);

    /**
     * Recherche des médicaments par type
     */
    List<Medicament> findByType(String type);

    /**
     * Recherche des médicaments par forme (comprimé, sirop, etc.)
     */
    List<Medicament> findByForme(String forme);

    /**
     * Recherche des médicaments remboursables ou non
     */
    List<Medicament> findByRemboursable(Boolean remboursable);

    /**
     * Recherche des médicaments par fourchette de prix
     */
    List<Medicament> findByPrixRange(Double prixMin, Double prixMax);

    /**
     * Recherche avancée sur plusieurs critères
     */
    List<Medicament> advancedSearch(MedicamentSearchCriteria criteria);

    // ==================== STATISTIQUES ET ANALYSES ====================

    /**
     * Compte le nombre total de médicaments
     */
    long count();

    /**
     * Récupère les statistiques des médicaments
     */
    MedicamentStatistics getStatistics();

    /**
     * Récupère les médicaments les plus chers
     */
    List<Medicament> getMostExpensiveMedicaments(int limit);

    /**
     * Récupère les médicaments les moins chers
     */
    List<Medicament> getCheapestMedicaments(int limit);

    /**
     * Calcule la valeur totale du stock
     */
    Double calculateTotalStockValue();

    // ==================== GESTION DES PRIX ====================

    /**
     * Met à jour le prix d'un médicament
     */
    Medicament updatePrice(Long id, Double nouveauPrix);

    /**
     * Applique une remise en pourcentage sur un médicament
     */
    Medicament applyDiscount(Long id, Double pourcentageRemise);

    /**
     * Vérifie si un prix est valide
     */
    boolean isValidPrice(Double prix);

    // ==================== VALIDATION ET UTILITAIRES ====================

    /**
     * Vérifie si un médicament existe déjà
     */
    boolean existsById(Long id);

    /**
     * Vérifie si un nom de médicament est déjà utilisé
     */
    boolean isNameTaken(String nom);

    /**
     * Valide les données d'un médicament
     */
    ValidationResult validateMedicament(Medicament medicament);

    /**
     * Génère un code de médicament unique
     */
    String generateMedicamentCode();

    // ==================== INTERFACES DES DTOs ====================

    /**
     * Critères de recherche avancée
     */
    interface MedicamentSearchCriteria {
        String getNom();
        void setNom(String nom);

        String getType();
        void setType(String type);

        String getForme();
        void setForme(String forme);

        Boolean getRemboursable();
        void setRemboursable(Boolean remboursable);

        Double getPrixMin();
        void setPrixMin(Double prixMin);

        Double getPrixMax();
        void setPrixMax(Double prixMax);

        String getDescriptionKeyword();
        void setDescriptionKeyword(String descriptionKeyword);
    }

    /**
     * Statistiques des médicaments
     */
    interface MedicamentStatistics {
        long getTotalMedicaments();
        void setTotalMedicaments(long totalMedicaments);

        long getRemboursablesCount();
        void setRemboursablesCount(long remboursablesCount);

        long getNonRemboursablesCount();
        void setNonRemboursablesCount(long nonRemboursablesCount);

        Double getPrixMoyen();
        void setPrixMoyen(Double prixMoyen);

        Double getPrixMax();
        void setPrixMax(Double prixMax);

        Double getPrixMin();
        void setPrixMin(Double prixMin);

        java.util.Map<String, Long> getRepartitionParType();
        void setRepartitionParType(java.util.Map<String, Long> repartitionParType);

        java.util.Map<String, Long> getRepartitionParForme();
        void setRepartitionParForme(java.util.Map<String, Long> repartitionParForme);
    }

    /**
     * Résultat de validation
     */
    interface ValidationResult {
        boolean isValid();
        void setValid(boolean valid);

        java.util.List<String> getErrors();
        void setErrors(java.util.List<String> errors);

        java.util.List<String> getWarnings();
        void setWarnings(java.util.List<String> warnings);

        boolean hasErrors();
        boolean hasWarnings();
    }

    // ==================== EXCEPTIONS ====================

    /**
     * Exception pour médicament non trouvé
     */
    class MedicamentNotFoundException extends RuntimeException {
        public MedicamentNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Exception pour validation de médicament
     */
    class MedicamentValidationException extends RuntimeException {
        public MedicamentValidationException(String message) {
            super(message);
        }
    }

    /**
     * Exception pour prix invalide
     */
    class InvalidPriceException extends RuntimeException {
        public InvalidPriceException(String message) {
            super(message);
        }
    }

    /**
     * Exception pour duplication de nom
     */
    class DuplicateNameException extends RuntimeException {
        public DuplicateNameException(String message) {
            super(message);
        }
    }
}