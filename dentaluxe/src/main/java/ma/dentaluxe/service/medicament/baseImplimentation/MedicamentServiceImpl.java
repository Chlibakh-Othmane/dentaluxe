package ma.dentaluxe.service.medicament.baseImplementation;

import ma.dentaluxe.service.medicament.api.MedicamentService;
import ma.dentaluxe.repository.modules.medicament.api.MedicamentRepository;
import ma.dentaluxe.entities.ordonnance.Medicament;
import java.util.*;

/**
 * Implémentation du service de gestion des médicaments.
 *
 * @author CHLIBAKH OTHMANE
 */
public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepository medicamentRepository;

    public MedicamentServiceImpl(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    // ==================== IMPLÉMENTATION DES DTOs ====================

    public static class MedicamentSearchCriteriaImpl implements MedicamentSearchCriteria {
        private String nom;
        private String type;
        private String forme;
        private Boolean remboursable;
        private Double prixMin;
        private Double prixMax;
        private String descriptionKeyword;

        public MedicamentSearchCriteriaImpl() {}

        @Override public String getNom() { return nom; }
        @Override public void setNom(String nom) { this.nom = nom; }

        @Override public String getType() { return type; }
        @Override public void setType(String type) { this.type = type; }

        @Override public String getForme() { return forme; }
        @Override public void setForme(String forme) { this.forme = forme; }

        @Override public Boolean getRemboursable() { return remboursable; }
        @Override public void setRemboursable(Boolean remboursable) { this.remboursable = remboursable; }

        @Override public Double getPrixMin() { return prixMin; }
        @Override public void setPrixMin(Double prixMin) { this.prixMin = prixMin; }

        @Override public Double getPrixMax() { return prixMax; }
        @Override public void setPrixMax(Double prixMax) { this.prixMax = prixMax; }

        @Override public String getDescriptionKeyword() { return descriptionKeyword; }
        @Override public void setDescriptionKeyword(String descriptionKeyword) { this.descriptionKeyword = descriptionKeyword; }
    }

    public static class MedicamentStatisticsImpl implements MedicamentStatistics {
        private long totalMedicaments;
        private long remboursablesCount;
        private long nonRemboursablesCount;
        private Double prixMoyen;
        private Double prixMax;
        private Double prixMin;
        private Map<String, Long> repartitionParType;
        private Map<String, Long> repartitionParForme;

        public MedicamentStatisticsImpl() {
            repartitionParType = new HashMap<>();
            repartitionParForme = new HashMap<>();
        }

        @Override public long getTotalMedicaments() { return totalMedicaments; }
        @Override public void setTotalMedicaments(long totalMedicaments) { this.totalMedicaments = totalMedicaments; }

        @Override public long getRemboursablesCount() { return remboursablesCount; }
        @Override public void setRemboursablesCount(long remboursablesCount) { this.remboursablesCount = remboursablesCount; }

        @Override public long getNonRemboursablesCount() { return nonRemboursablesCount; }
        @Override public void setNonRemboursablesCount(long nonRemboursablesCount) { this.nonRemboursablesCount = nonRemboursablesCount; }

        @Override public Double getPrixMoyen() { return prixMoyen; }
        @Override public void setPrixMoyen(Double prixMoyen) { this.prixMoyen = prixMoyen; }

        @Override public Double getPrixMax() { return prixMax; }
        @Override public void setPrixMax(Double prixMax) { this.prixMax = prixMax; }

        @Override public Double getPrixMin() { return prixMin; }
        @Override public void setPrixMin(Double prixMin) { this.prixMin = prixMin; }

        @Override public Map<String, Long> getRepartitionParType() { return repartitionParType; }
        @Override public void setRepartitionParType(Map<String, Long> repartitionParType) { this.repartitionParType = repartitionParType; }

        @Override public Map<String, Long> getRepartitionParForme() { return repartitionParForme; }
        @Override public void setRepartitionParForme(Map<String, Long> repartitionParForme) { this.repartitionParForme = repartitionParForme; }
    }

    public static class ValidationResultImpl implements ValidationResult {
        private boolean valid;
        private List<String> errors;
        private List<String> warnings;

        public ValidationResultImpl() {
            this.valid = true;
            this.errors = new ArrayList<>();
            this.warnings = new ArrayList<>();
        }

        @Override public boolean isValid() { return valid; }
        @Override public void setValid(boolean valid) { this.valid = valid; }

        @Override public List<String> getErrors() { return errors; }
        @Override public void setErrors(List<String> errors) { this.errors = errors; }

        @Override public List<String> getWarnings() { return warnings; }
        @Override public void setWarnings(List<String> warnings) { this.warnings = warnings; }

        @Override public boolean hasErrors() { return !errors.isEmpty(); }
        @Override public boolean hasWarnings() { return !warnings.isEmpty(); }

        public void addError(String error) {
            errors.add(error);
            valid = false;
        }

        public void addWarning(String warning) {
            warnings.add(warning);
        }
    }

    // ==================== IMPLÉMENTATION DES MÉTHODES ====================

    @Override
    public List<Medicament> findAll() {
        return medicamentRepository.findAll();
    }

    @Override
    public Medicament findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID médicament invalide");
        }

        Medicament medicament = medicamentRepository.findById(id);
        if (medicament == null) {
            throw new MedicamentNotFoundException("Médicament non trouvé avec l'ID: " + id);
        }
        return medicament;
    }

    @Override
    public Medicament create(Medicament medicament) {
        System.out.println("Création d'un nouveau médicament...");

        // Validation
        ValidationResult validationResult = validateMedicament(medicament);
        if (!validationResult.isValid()) {
            throw new MedicamentValidationException("Validation échouée: " + String.join(", ", validationResult.getErrors()));
        }

        // Vérifier l'unicité du nom
        if (isNameTaken(medicament.getNom())) {
            throw new DuplicateNameException("Un médicament avec le nom '" + medicament.getNom() + "' existe déjà");
        }

        // Validation du prix
        if (!isValidPrice(medicament.getPrixUnitaire())) {
            throw new InvalidPriceException("Prix unitaire invalide: " + medicament.getPrixUnitaire());
        }

        // Sauvegarde
        medicamentRepository.create(medicament);
        System.out.println("Médicament créé avec ID: " + medicament.getIdMedicament());

        return medicament;
    }

    @Override
    public Medicament update(Long id, Medicament medicament) {
        // Vérifier que le médicament existe
        Medicament existing = findById(id);

        // Appliquer les modifications
        boolean modifications = false;

        if (medicament.getNom() != null && !medicament.getNom().equals(existing.getNom())) {
            // Vérifier que le nouveau nom n'est pas utilisé par un autre médicament
            List<Medicament> medicamentsAvecMemeNom = medicamentRepository.findByNom(medicament.getNom());
            if (!medicamentsAvecMemeNom.isEmpty() && !medicamentsAvecMemeNom.get(0).getIdMedicament().equals(id)) {
                throw new DuplicateNameException("Ce nom est déjà utilisé par un autre médicament");
            }
            existing.setNom(medicament.getNom());
            modifications = true;
        }

        if (medicament.getType() != null && !medicament.getType().equals(existing.getType())) {
            existing.setType(medicament.getType());
            modifications = true;
        }

        if (medicament.getForme() != null && !medicament.getForme().equals(existing.getForme())) {
            existing.setForme(medicament.getForme());
            modifications = true;
        }

        if (medicament.getRemboursable() != null && !medicament.getRemboursable().equals(existing.getRemboursable())) {
            existing.setRemboursable(medicament.getRemboursable());
            modifications = true;
        }

        if (medicament.getPrixUnitaire() != null && !medicament.getPrixUnitaire().equals(existing.getPrixUnitaire())) {
            if (!isValidPrice(medicament.getPrixUnitaire())) {
                throw new InvalidPriceException("Prix unitaire invalide: " + medicament.getPrixUnitaire());
            }
            existing.setPrixUnitaire(medicament.getPrixUnitaire());
            modifications = true;
        }

        if (medicament.getDescription() != null && !medicament.getDescription().equals(existing.getDescription())) {
            existing.setDescription(medicament.getDescription());
            modifications = true;
        }

        // Valider les modifications
        ValidationResult validationResult = validateMedicament(existing);
        if (!validationResult.isValid()) {
            throw new MedicamentValidationException("Validation échouée après modifications: " +
                    String.join(", ", validationResult.getErrors()));
        }

        // Sauvegarder si modifications
        if (modifications) {
            medicamentRepository.update(existing);
            System.out.println("Médicament mis à jour: " + id);
        }

        return existing;
    }

    @Override
    public void delete(Long id) {
        Medicament medicament = findById(id);
        medicamentRepository.deleteById(id);
        System.out.println("Médicament supprimé: " + id);
    }

    @Override
    public List<Medicament> findByNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return findAll();
        }
        return medicamentRepository.findByNom(nom.trim());
    }

    @Override
    public List<Medicament> findByType(String type) {
        if (type == null || type.trim().isEmpty()) {
            return findAll();
        }
        return medicamentRepository.findByType(type.trim());
    }

    @Override
    public List<Medicament> findByForme(String forme) {
        if (forme == null || forme.trim().isEmpty()) {
            return findAll();
        }
        return medicamentRepository.findByForme(forme.trim());
    }

    @Override
    public List<Medicament> findByRemboursable(Boolean remboursable) {
        if (remboursable == null) {
            return findAll();
        }
        return medicamentRepository.findByRemboursable(remboursable);
    }

    @Override
    public List<Medicament> findByPrixRange(Double prixMin, Double prixMax) {
        if (prixMin == null || prixMax == null || prixMin < 0 || prixMax < 0 || prixMin > prixMax) {
            throw new IllegalArgumentException("Fourchette de prix invalide");
        }

        List<Medicament> tousMedicaments = findAll();
        List<Medicament> resultats = new ArrayList<>();

        for (Medicament m : tousMedicaments) {
            if (m.getPrixUnitaire() != null &&
                    m.getPrixUnitaire() >= prixMin &&
                    m.getPrixUnitaire() <= prixMax) {
                resultats.add(m);
            }
        }

        return resultats;
    }

    @Override
    public List<Medicament> advancedSearch(MedicamentSearchCriteria criteria) {
        if (criteria == null) {
            return findAll();
        }

        List<Medicament> tousMedicaments = findAll();
        List<Medicament> resultats = new ArrayList<>();

        for (Medicament m : tousMedicaments) {
            if (matchesCriteria(m, criteria)) {
                resultats.add(m);
            }
        }

        return resultats;
    }

    @Override
    public long count() {
        return findAll().size();
    }

    @Override
    public MedicamentStatistics getStatistics() {
        MedicamentStatisticsImpl stats = new MedicamentStatisticsImpl();

        List<Medicament> medicaments = findAll();
        stats.setTotalMedicaments(medicaments.size());

        // Calcul par remboursabilité
        long remboursables = medicaments.stream().filter(m -> m.getRemboursable() != null && m.getRemboursable()).count();
        long nonRemboursables = medicaments.size() - remboursables;
        stats.setRemboursablesCount(remboursables);
        stats.setNonRemboursablesCount(nonRemboursables);

        // Calcul des prix
        List<Double> prix = new ArrayList<>();
        Double prixMin = null;
        Double prixMax = null;

        for (Medicament m : medicaments) {
            if (m.getPrixUnitaire() != null) {
                prix.add(m.getPrixUnitaire());

                if (prixMin == null || m.getPrixUnitaire() < prixMin) {
                    prixMin = m.getPrixUnitaire();
                }

                if (prixMax == null || m.getPrixUnitaire() > prixMax) {
                    prixMax = m.getPrixUnitaire();
                }
            }
        }

        if (!prix.isEmpty()) {
            double somme = 0;
            for (Double p : prix) {
                somme += p;
            }
            stats.setPrixMoyen(somme / prix.size());
            stats.setPrixMin(prixMin);
            stats.setPrixMax(prixMax);
        }

        // Répartition par type
        Map<String, Long> repartitionType = new HashMap<>();
        for (Medicament m : medicaments) {
            String type = m.getType() != null ? m.getType() : "Non spécifié";
            repartitionType.put(type, repartitionType.getOrDefault(type, 0L) + 1);
        }
        stats.setRepartitionParType(repartitionType);

        // Répartition par forme
        Map<String, Long> repartitionForme = new HashMap<>();
        for (Medicament m : medicaments) {
            String forme = m.getForme() != null ? m.getForme() : "Non spécifiée";
            repartitionForme.put(forme, repartitionForme.getOrDefault(forme, 0L) + 1);
        }
        stats.setRepartitionParForme(repartitionForme);

        return stats;
    }

    @Override
    public List<Medicament> getMostExpensiveMedicaments(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("La limite doit être positive");
        }

        List<Medicament> medicaments = findAll();
        medicaments.sort((m1, m2) -> {
            Double p1 = m1.getPrixUnitaire() != null ? m1.getPrixUnitaire() : 0.0;
            Double p2 = m2.getPrixUnitaire() != null ? m2.getPrixUnitaire() : 0.0;
            return p2.compareTo(p1); // Ordre décroissant
        });

        return medicaments.stream().limit(limit).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<Medicament> getCheapestMedicaments(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("La limite doit être positive");
        }

        List<Medicament> medicaments = findAll();
        medicaments.sort((m1, m2) -> {
            Double p1 = m1.getPrixUnitaire() != null ? m1.getPrixUnitaire() : Double.MAX_VALUE;
            Double p2 = m2.getPrixUnitaire() != null ? m2.getPrixUnitaire() : Double.MAX_VALUE;
            return p1.compareTo(p2); // Ordre croissant
        });

        return medicaments.stream().limit(limit).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Double calculateTotalStockValue() {
        List<Medicament> medicaments = findAll();
        double total = 0.0;

        for (Medicament m : medicaments) {
            if (m.getPrixUnitaire() != null) {
                total += m.getPrixUnitaire();
            }
        }

        return total;
    }

    @Override
    public Medicament updatePrice(Long id, Double nouveauPrix) {
        if (!isValidPrice(nouveauPrix)) {
            throw new InvalidPriceException("Prix invalide: " + nouveauPrix);
        }

        Medicament medicament = findById(id);
        medicament.setPrixUnitaire(nouveauPrix);
        medicamentRepository.update(medicament);

        System.out.println("Prix mis à jour pour le médicament ID " + id + ": " + nouveauPrix + " DH");
        return medicament;
    }

    @Override
    public Medicament applyDiscount(Long id, Double pourcentageRemise) {
        if (pourcentageRemise < 0 || pourcentageRemise > 100) {
            throw new InvalidPriceException("Pourcentage de remise invalide: " + pourcentageRemise + "%");
        }

        Medicament medicament = findById(id);
        Double ancienPrix = medicament.getPrixUnitaire();

        if (ancienPrix == null) {
            throw new InvalidPriceException("Le médicament n'a pas de prix défini");
        }

        Double nouveauPrix = ancienPrix * (1 - pourcentageRemise / 100);

        if (!isValidPrice(nouveauPrix)) {
            throw new InvalidPriceException("Nouveau prix invalide après remise: " + nouveauPrix);
        }

        medicament.setPrixUnitaire(nouveauPrix);
        medicamentRepository.update(medicament);

        System.out.println("Remise appliquée au médicament ID " + id + ": " +
                ancienPrix + " DH → " + nouveauPrix + " DH (" + pourcentageRemise + "% de remise)");

        return medicament;
    }

    @Override
    public boolean isValidPrice(Double prix) {
        if (prix == null) {
            return false;
        }
        return prix >= 0 && prix <= 1000000; // Prix entre 0 et 1,000,000 DH
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        try {
            findById(id);
            return true;
        } catch (MedicamentNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isNameTaken(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return false;
        }
        List<Medicament> medicaments = medicamentRepository.findByNom(nom.trim());
        return !medicaments.isEmpty();
    }

    @Override
    public ValidationResult validateMedicament(Medicament medicament) {
        ValidationResultImpl result = new ValidationResultImpl();

        if (medicament == null) {
            result.addError("Le médicament ne peut pas être null");
            return result;
        }

        // Validation du nom
        if (medicament.getNom() == null || medicament.getNom().trim().isEmpty()) {
            result.addError("Le nom du médicament est obligatoire");
        } else if (medicament.getNom().length() > 100) {
            result.addError("Le nom ne peut pas dépasser 100 caractères");
        }

        // Validation du type
        if (medicament.getType() == null || medicament.getType().trim().isEmpty()) {
            result.addWarning("Le type n'est pas spécifié");
        }

        // Validation de la forme
        if (medicament.getForme() == null || medicament.getForme().trim().isEmpty()) {
            result.addWarning("La forme n'est pas spécifiée");
        }

        // Validation du prix
        if (medicament.getPrixUnitaire() == null) {
            result.addError("Le prix unitaire est obligatoire");
        } else if (!isValidPrice(medicament.getPrixUnitaire())) {
            result.addError("Prix unitaire invalide (doit être entre 0 et 1,000,000 DH)");
        }

        // Validation de la description (optionnelle)
        if (medicament.getDescription() != null && medicament.getDescription().length() > 500) {
            result.addWarning("La description est trop longue (max 500 caractères)");
        }

        return result;
    }

    @Override
    public String generateMedicamentCode() {
        // Génère un code unique basé sur le timestamp et un random
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int)(Math.random() * 1000));
        return "MED" + timestamp.substring(timestamp.length() - 6) + random;
    }

    // ==================== MÉTHODES PRIVÉES ====================

    private boolean matchesCriteria(Medicament medicament, MedicamentSearchCriteria criteria) {
        // Filtre par nom
        if (criteria.getNom() != null && !criteria.getNom().trim().isEmpty()) {
            if (medicament.getNom() == null ||
                    !medicament.getNom().toLowerCase().contains(criteria.getNom().toLowerCase())) {
                return false;
            }
        }

        // Filtre par type
        if (criteria.getType() != null && !criteria.getType().trim().isEmpty()) {
            if (medicament.getType() == null ||
                    !medicament.getType().equalsIgnoreCase(criteria.getType().trim())) {
                return false;
            }
        }

        // Filtre par forme
        if (criteria.getForme() != null && !criteria.getForme().trim().isEmpty()) {
            if (medicament.getForme() == null ||
                    !medicament.getForme().equalsIgnoreCase(criteria.getForme().trim())) {
                return false;
            }
        }

        // Filtre par remboursable
        if (criteria.getRemboursable() != null) {
            if (medicament.getRemboursable() == null ||
                    !medicament.getRemboursable().equals(criteria.getRemboursable())) {
                return false;
            }
        }

        // Filtre par prix minimum
        if (criteria.getPrixMin() != null) {
            if (medicament.getPrixUnitaire() == null ||
                    medicament.getPrixUnitaire() < criteria.getPrixMin()) {
                return false;
            }
        }

        // Filtre par prix maximum
        if (criteria.getPrixMax() != null) {
            if (medicament.getPrixUnitaire() == null ||
                    medicament.getPrixUnitaire() > criteria.getPrixMax()) {
                return false;
            }
        }

        // Filtre par mot-clé dans la description
        if (criteria.getDescriptionKeyword() != null && !criteria.getDescriptionKeyword().trim().isEmpty()) {
            if (medicament.getDescription() == null ||
                    !medicament.getDescription().toLowerCase().contains(criteria.getDescriptionKeyword().toLowerCase())) {
                return false;
            }
        }

        return true;
    }
}