// AUTEUR : AYA LEZREGUE
package ma.dentaluxe.service.actes.baseImplementation;

import ma.dentaluxe.mvc.dto.ActeDTO;
import ma.dentaluxe.entities.acte.Acte;
import ma.dentaluxe.entities.enums.CategorieActe;
import ma.dentaluxe.repository.modules.actes.api.ActeRepository;
import ma.dentaluxe.service.actes.api.ActeService;

import java.util.*;
import java.util.stream.Collectors;

public class ActeServiceImpl implements ActeService {

    private final ActeRepository acteRepository;

    public ActeServiceImpl(ActeRepository acteRepository) {
        this.acteRepository = acteRepository;
    }

    // ========== CRUD de base ==========

    @Override
    public ActeDTO createActe(ActeDTO acteDTO) {
        // Validation
        if (!validateActe(acteDTO)) {
            throw new IllegalArgumentException("Acte invalide : données manquantes ou incorrectes");
        }

        // Vérifier si un acte avec le même libellé existe déjà
        if (acteExistsByLibelle(acteDTO.getLibelle())) {
            throw new IllegalStateException("Un acte avec ce libellé existe déjà : " + acteDTO.getLibelle());
        }

        // Convertir DTO -> Entity
        Acte acte = convertToEntity(acteDTO);

        // Créer l'acte
        acteRepository.create(acte);

        System.out.println("✅ Acte créé avec succès : " + acte.getLibelle() + " (ID: " + acte.getIdActe() + ")");

        return convertToDTO(acte);
    }

    @Override
    public ActeDTO getActeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        Acte acte = acteRepository.findById(id);
        if (acte == null) {
            throw new IllegalStateException("Acte introuvable (ID: " + id + ")");
        }

        return convertToDTO(acte);
    }

    @Override
    public List<ActeDTO> getAllActes() {
        return acteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActeDTO updateActe(ActeDTO acteDTO) {
        if (acteDTO == null || acteDTO.getIdActe() == null) {
            throw new IllegalArgumentException("Acte ou ID invalide");
        }

        // Vérifier que l'acte existe
        if (!acteExists(acteDTO.getIdActe())) {
            throw new IllegalStateException("Acte introuvable (ID: " + acteDTO.getIdActe() + ")");
        }

        // Validation
        if (!validateActe(acteDTO)) {
            throw new IllegalArgumentException("Acte invalide : données manquantes ou incorrectes");
        }

        // Convertir et mettre à jour
        Acte acte = convertToEntity(acteDTO);
        acteRepository.update(acte);

        System.out.println("✅ Acte mis à jour : " + acte.getLibelle());

        return convertToDTO(acte);
    }

    @Override
    public void deleteActe(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }

        if (!acteExists(id)) {
            throw new IllegalStateException("Acte introuvable (ID: " + id + ")");
        }

        acteRepository.deleteById(id);
        System.out.println("🗑️ Acte supprimé (ID: " + id + ")");
    }

    // ========== Recherche et filtres ==========

    @Override
    public List<ActeDTO> getActesByCategorie(CategorieActe categorie) {
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne peut pas être null");
        }

        return acteRepository.findByCategorie(categorie).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActeDTO> searchActesByLibelle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot-clé ne peut pas être vide");
        }

        return acteRepository.searchByLibelle(keyword).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActeDTO getActeByLibelle(String libelle) {
        if (libelle == null || libelle.trim().isEmpty()) {
            throw new IllegalArgumentException("Le libellé ne peut pas être vide");
        }

        List<Acte> actes = acteRepository.searchByLibelle(libelle);
        Acte acte = actes.stream()
                .filter(a -> a.getLibelle().equalsIgnoreCase(libelle))
                .findFirst()
                .orElse(null);

        return acte != null ? convertToDTO(acte) : null;
    }

    @Override
    public List<ActeDTO> getActesByPriceRange(double prixMin, double prixMax) {
        if (prixMin < 0 || prixMax < 0) {
            throw new IllegalArgumentException("Les prix doivent être positifs");
        }
        if (prixMin > prixMax) {
            throw new IllegalArgumentException("Le prix min doit être inférieur ou égal au prix max");
        }

        return getAllActes().stream()
                .filter(a -> a.getPrixDeBase() >= prixMin && a.getPrixDeBase() <= prixMax)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActeDTO> getActesSortedByPrice(boolean ascending) {
        List<ActeDTO> actes = getAllActes();

        if (ascending) {
            actes.sort(Comparator.comparing(ActeDTO::getPrixDeBase));
        } else {
            actes.sort(Comparator.comparing(ActeDTO::getPrixDeBase).reversed());
        }

        return actes;
    }

    @Override
    public List<ActeDTO> getActesSortedByLibelle() {
        List<ActeDTO> actes = getAllActes();
        actes.sort(Comparator.comparing(ActeDTO::getLibelle));
        return actes;
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
        List<ActeDTO> actes = getAllActes();
        if (actes.isEmpty()) {
            return 0.0;
        }

        return actes.stream()
                .mapToDouble(ActeDTO::getPrixDeBase)
                .average()
                .orElse(0.0);
    }

    @Override
    public ActeDTO getMostExpensiveActe() {
        List<ActeDTO> actes = getAllActes();
        if (actes.isEmpty()) {
            return null;
        }

        return actes.stream()
                .max(Comparator.comparing(ActeDTO::getPrixDeBase))
                .orElse(null);
    }

    @Override
    public ActeDTO getCheapestActe() {
        List<ActeDTO> actes = getAllActes();
        if (actes.isEmpty()) {
            return null;
        }

        return actes.stream()
                .min(Comparator.comparing(ActeDTO::getPrixDeBase))
                .orElse(null);
    }

    @Override
    public double getTotalPrixActes() {
        return getAllActes().stream()
                .mapToDouble(ActeDTO::getPrixDeBase)
                .sum();
    }

    @Override
    public List<CategorieStatDTO> getStatistiquesByCategorie() {
        List<CategorieStatDTO> stats = new ArrayList<>();

        for (CategorieActe categorie : CategorieActe.values()) {
            List<ActeDTO> actesCategorie = getActesByCategorie(categorie);

            if (!actesCategorie.isEmpty()) {
                CategorieStatDTO stat = new CategorieStatDTO();
                stat.setCategorie(categorie);
                stat.setNombreActes(actesCategorie.size());
                stat.setPrixMoyen(actesCategorie.stream()
                        .mapToDouble(ActeDTO::getPrixDeBase)
                        .average()
                        .orElse(0.0));
                stat.setPrixTotal(actesCategorie.stream()
                        .mapToDouble(ActeDTO::getPrixDeBase)
                        .sum());
                stat.setPrixMin(actesCategorie.stream()
                        .mapToDouble(ActeDTO::getPrixDeBase)
                        .min()
                        .orElse(0.0));
                stat.setPrixMax(actesCategorie.stream()
                        .mapToDouble(ActeDTO::getPrixDeBase)
                        .max()
                        .orElse(0.0));

                stats.add(stat);
            }
        }

        return stats;
    }

    // ========== Validation métier ==========

    @Override
    public boolean validateActe(ActeDTO acteDTO) {
        if (acteDTO == null) return false;
        if (acteDTO.getLibelle() == null || acteDTO.getLibelle().trim().isEmpty()) return false;
        if (acteDTO.getPrixDeBase() == null || acteDTO.getPrixDeBase() <= 0) return false;
        if (acteDTO.getCategorie() == null) return false;

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
    public ActeDTO updatePrix(Long idActe, double nouveauPrix) {
        if (nouveauPrix <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à 0");
        }

        ActeDTO acteDTO = getActeById(idActe);
        double ancienPrix = acteDTO.getPrixDeBase();

        acteDTO.setPrixDeBase(nouveauPrix);

        Acte acte = convertToEntity(acteDTO);
        acteRepository.update(acte);

        System.out.println("💰 Prix mis à jour : " + acteDTO.getLibelle());
        System.out.println("   Ancien prix : " + ancienPrix + " DH");
        System.out.println("   Nouveau prix : " + nouveauPrix + " DH");

        return acteDTO;
    }

    @Override
    public ActeDTO applyDiscount(Long idActe, double pourcentage) {
        if (pourcentage < 0 || pourcentage > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 100");
        }

        ActeDTO acteDTO = getActeById(idActe);
        double ancienPrix = acteDTO.getPrixDeBase();
        double nouveauPrix = ancienPrix * (1 - pourcentage / 100);

        acteDTO.setPrixDeBase(nouveauPrix);

        Acte acte = convertToEntity(acteDTO);
        acteRepository.update(acte);

        System.out.println("🎁 Remise appliquée : " + acteDTO.getLibelle());
        System.out.println("   Remise : " + pourcentage + "%");
        System.out.println("   Ancien prix : " + ancienPrix + " DH");
        System.out.println("   Nouveau prix : " + nouveauPrix + " DH");

        return acteDTO;
    }

    @Override
    public List<ActeDTO> applyDiscountToCategorie(CategorieActe categorie, double pourcentage) {
        if (categorie == null) {
            throw new IllegalArgumentException("La catégorie ne peut pas être null");
        }
        if (pourcentage < 0 || pourcentage > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 100");
        }

        List<ActeDTO> actesCategorie = getActesByCategorie(categorie);
        List<ActeDTO> actesModifies = new ArrayList<>();

        for (ActeDTO acteDTO : actesCategorie) {
            ActeDTO acteModifie = applyDiscount(acteDTO.getIdActe(), pourcentage);
            actesModifies.add(acteModifie);
        }

        System.out.println("🎁 Remise de " + pourcentage + "% appliquée à " + actesModifies.size() + " actes de la catégorie " + categorie);

        return actesModifies;
    }

    @Override
    public double calculatePrixAvecRemise(Long idActe, double pourcentage) {
        if (pourcentage < 0 || pourcentage > 100) {
            throw new IllegalArgumentException("Le pourcentage doit être entre 0 et 100");
        }

        ActeDTO acteDTO = getActeById(idActe);
        return acteDTO.getPrixDeBase() * (1 - pourcentage / 100);
    }

    // ========== Méthodes de conversion DTO <-> Entity ==========

    private ActeDTO convertToDTO(Acte acte) {
        if (acte == null) {
            return null;
        }

        ActeDTO dto = ActeDTO.builder()
                .idActe(acte.getIdActe())
                .idInterventionMedecin(acte.getIdInterventionMedecin())
                .libelle(acte.getLibelle())
                .description(acte.getDescription())
                .prixDeBase(acte.getPrixDeBase())
                .categorie(acte.getCategorie())
                .build();

        // Enrichir le DTO avec des informations supplémentaires
        if (acte.getCategorie() != null) {
            dto.setCategorieLibelle(acte.getCategorie().name());
        }

        return dto;
    }

    private Acte convertToEntity(ActeDTO dto) {
        if (dto == null) {
            return null;
        }

        return Acte.builder()
                .idActe(dto.getIdActe())
                .idInterventionMedecin(dto.getIdInterventionMedecin())
                .libelle(dto.getLibelle())
                .description(dto.getDescription())
                .prixDeBase(dto.getPrixDeBase())
                .categorie(dto.getCategorie())
                .build();
    }
}