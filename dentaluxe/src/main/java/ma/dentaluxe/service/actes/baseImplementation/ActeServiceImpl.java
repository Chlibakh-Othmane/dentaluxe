// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.actes.baseImplementation;

import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import ma.dentaluxe.repository.modules.actes.inMemDB_implementation.ActeRepositoryImpl;
import ma.dentaluxe.service.actes.api.ActeService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ActeServiceImpl implements ActeService {

    private final ActeRepository acteRepository;

    public ActeServiceImpl() {
        this.acteRepository = new ActeRepositoryImpl();
    }
// ========== CRUD de base ==========
@Override
public void createActe(Acte acte) {
    // Validation
    if (!validateActe(acte)) {
        throw new IllegalArgumentException("Acte invalide : données manquantes");
    }

    // Vérifier si un acte avec le même libellé existe déjà
    if (acteExistsByLibelle(acte.getLibelle())) {
        throw new IllegalStateException("Un acte avec ce libellé existe déjà");
    }

    // Créer l'acte
    acteRepository.create(acte);
    System.out.println(" Acte créé avec succès : " + acte.getLibelle() + " (ID: " + acte.getIdActe() + ")");
}
    @Override
    public Acte getActeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Acte acte = acteRepository.findById(id);
        if (acte == null) {
            throw new IllegalStateException("Acte introuvable (ID: " + id + ")");
        }

        return acte;
    }
    @Override
    public List<Acte> getAllActes() {
        return acteRepository.findAll();
    }
    @Override
    public void updateActe(Acte acte) {
        if (acte == null || acte.getIdActe() == null) {
            throw new IllegalArgumentException("Acte ou ID invalide");
        }

        // Vérifier que l'acte existe
        if (!acteExists(acte.getIdActe())) {
            throw new IllegalStateException("Acte introuvable");
        }

        // Validation
        if (!validateActe(acte)) {
            throw new IllegalArgumentException("Acte invalide : données manquantes");
        }

        acteRepository.update(acte);
        System.out.println(" Acte mis à jour : " + acte.getLibelle());
    }
    @Override
    public void deleteActe(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        if (!acteExists(id)) {
            throw new IllegalStateException("Acte introuvable");
        }

        acteRepository.deleteById(id);
        System.out.println(" Acte supprimé (ID: " + id + ")");
    }
    // ========== Recherche et filtres ==========

    @Override
    public List<Acte> getActesByCategorie(CategorieActe categorie) {
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne peut pas être null");
        }
        return acteRepository.findByCategorie(categorie);
    }
    @Override
    public List<Acte> searchActesByLibelle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot-clé ne peut pas être vide");
        }
        return acteRepository.searchByLibelle(keyword);
    }
    @Override
    public Acte getActeByLibelle(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé ne peut pas être vide");
        }

        List<Acte> actes = acteRepository.searchByLibelle(libelle);
        return actes.stream()
                .filter(a -> a.getLibelle().equalsIgnoreCase(libelle))
                .findFirst()
                .orElse(null);
    }
    // ========== Statistiques ==========

    @Override
    public int countActes() {
        return getAllActes().size();
    }

    @Override
    public int countActesByCategorie(CategorieActe categorie) {
        return getActesByCategorie(categorie).size();
    }
    @Override
    public double getAveragePrixActes() {
        List<Acte> actes = getAllActes();
        if (actes.isEmpty()) {
            return 0.0;
        }

        return actes.stream()
                .mapToDouble(Acte::getPrixDeBase)
                .average()
                .orElse(0.0);
    }
    @Override
    public Acte getMostExpensiveActe() {
        List<Acte> actes = getAllActes();
        if (actes.isEmpty()) {
            return null;
        }

        return actes.stream()
                .max(Comparator.comparing(Acte::getPrixDeBase))
                .orElse(null);
    }
    @Override
    public Acte getCheapestActe() {
        List<Acte> actes = getAllActes();
        if (actes.isEmpty()) {
            return null;
        }

        return actes.stream()
                .min(Comparator.comparing(Acte::getPrixDeBase))
                .orElse(null);
    }
    @Override
    public double getTotalPrixActes() {
        return getAllActes().stream()
                .mapToDouble(Acte::getPrixDeBase)
                .sum();
    }
    // ========== Validation métier ==========

    @Override
    public boolean validateActe(Acte acte) {
        if (acte == null) return false;
        if (acte.getLibelle() == null || acte.getLibelle().trim().isEmpty()) return false;
        if (acte.getPrixDeBase() == null || acte.getPrixDeBase() <= 0) return false;
        if (acte.getCategorie() == null) return false;

        return true;
    }
    @Override
    public boolean acteExists(Long id) {
        if (id == null) return false;
        try {
            return acteRepository.findById(id) != null;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public boolean acteExistsByLibelle(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) return false;
        return getActeByLibelle(libelle) != null;
    }
    // ========== Gestion des prix ==========

    @Override
    public void updatePrix(Long idActe, double nouveauPrix) {
        if (nouveauPrix <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }

        Acte acte = getActeById(idActe);
        double ancienPrix = acte.getPrixDeBase();

        acte.setPrixDeBase(nouveauPrix);
        acteRepository.update(acte);

        System.out.println(" Prix mis à jour : " + acte.getLibelle());
        System.out.println("   Ancien prix : " + ancienPrix + " DH");
        System.out.println("   Nouveau prix : " + nouveauPrix + " DH");
    }
    @Override
    public void applyDiscount(Long idActe, double pourcentage) {
        if (pourcentage < 0 || pourcentage > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 100");
        }

        Acte acte = getActeById(idActe);
        double ancienPrix = acte.getPrixDeBase();
        double nouveauPrix = ancienPrix * (1 - pourcentage / 100);

        acte.setPrixDeBase(nouveauPrix);
        acteRepository.update(acte);

        System.out.println(" Remise appliquée : " + acte.getLibelle());
        System.out.println("   Remise : " + pourcentage + "%");
        System.out.println("   Ancien prix : " + ancienPrix + " DH");
        System.out.println("   Nouveau prix : " + nouveauPrix + " DH");
    }
    @Override
    public List<Acte> getActesByPriceRange(double prixMin, double prixMax) {
        if (prixMin < 0 || prixMax < 0) {
            throw new IllegalArgumentException("Les prix doivent être positifs");
        }
        if (prixMin > prixMax) {
            throw new IllegalArgumentException("Le prix min doit être inférieur au prix max");
        }

        return getAllActes().stream()
                .filter(a -> a.getPrixDeBase() >= prixMin && a.getPrixDeBase() <= prixMax)
                .collect(Collectors.toList());
    }
}
