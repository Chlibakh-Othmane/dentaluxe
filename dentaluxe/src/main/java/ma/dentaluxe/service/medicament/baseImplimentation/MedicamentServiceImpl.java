package ma.dentaluxe.service.medicament.baseImplimentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.service.medicament.api.MedicamentService;
import ma.dentaluxe.repository.modules.medicament.api.MedicamentRepository;
import ma.dentaluxe.entities.ordonnance.Medicament;

// === USAGE DES PACKAGES SÉPARÉS (DTO) ===
import ma.dentaluxe.mvc.dto.medicament.MedicamentDTO;
import ma.dentaluxe.mvc.dto.medicament.MedicamentStatisticsDTO;

// === USAGE DES PACKAGES SÉPARÉS (EXCEPTIONS) ===
import ma.dentaluxe.common.exceptions.modules.medicament.MedicamentNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation rectifiée pour la gestion des médicaments.
 * @author CHLIBAKH OTHMANE
 */
public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepository medicamentRepository;

    // CONSTRUCTEUR VIDE : Obligatoire pour le fonctionnement avec ApplicationContext.newInstance()
    public MedicamentServiceImpl() {
        // On récupère le bean "medicamentRepo" défini dans beans.properties
        this.medicamentRepository = (MedicamentRepository) ApplicationContext.getBean("medicamentRepo");
    }

    @Override
    public List<MedicamentDTO> findAll() {
        return medicamentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedicamentDTO findById(Long id) {
        Medicament m = medicamentRepository.findById(id);
        if (m == null) {
            // Utilisation de l'exception personnalisée
            throw new MedicamentNotFoundException("ID: " + id);
        }
        return convertToDTO(m);
    }

    @Override
    public MedicamentDTO create(MedicamentDTO dto) {
        // Conversion DTO -> Entity pour la sauvegarde
        Medicament m = Medicament.builder()
                .nom(dto.getNom())
                .type(dto.getType())
                .forme(dto.getForme())
                .prixUnitaire(dto.getPrixUnitaire())
                .remboursable(dto.getRemboursable())
                .description(dto.getDescription())
                .build();

        medicamentRepository.create(m);
        return convertToDTO(m);
    }

    @Override
    public MedicamentDTO update(Long id, MedicamentDTO dto) {
        Medicament existing = medicamentRepository.findById(id);
        if (existing == null) throw new MedicamentNotFoundException("ID: " + id);

        existing.setNom(dto.getNom());
        existing.setPrixUnitaire(dto.getPrixUnitaire());
        existing.setRemboursable(dto.getRemboursable());

        medicamentRepository.update(existing);
        return convertToDTO(existing);
    }

    @Override
    public void delete(Long id) {
        if (medicamentRepository.findById(id) == null) throw new MedicamentNotFoundException("ID: " + id);
        medicamentRepository.deleteById(id);
    }

    @Override
    public List<MedicamentDTO> findByNom(String nom) {
        return medicamentRepository.findByNom(nom).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MedicamentDTO applyDiscount(Long id, Double pourcentage) {
        Medicament m = medicamentRepository.findById(id);
        if (m == null) throw new MedicamentNotFoundException("ID: " + id);

        Double nouveauPrix = m.getPrixUnitaire() * (1 - pourcentage / 100);
        m.setPrixUnitaire(nouveauPrix);

        medicamentRepository.update(m);
        return convertToDTO(m);
    }

    @Override
    public MedicamentStatisticsDTO getStatistics() {
        List<Medicament> all = medicamentRepository.findAll();
        return MedicamentStatisticsDTO.builder()
                .totalMedicaments(all.size())
                .remboursablesCount(all.stream().filter(m -> m.getRemboursable()).count())
                .prixMoyen(all.stream().mapToDouble(m -> m.getPrixUnitaire()).average().orElse(0.0))
                .build();
    }

    // ==================== MÉTHODE DE MAPPING PRIVÉE ====================
    // Transforme l'objet de la base de données (Entity) en objet pour la vue (DTO)
    private MedicamentDTO convertToDTO(Medicament m) {
        return MedicamentDTO.builder()
                .idMedicament(m.getIdMedicament())
                .nom(m.getNom())
                .type(m.getType())
                .forme(m.getForme())
                .prixUnitaire(m.getPrixUnitaire())
                .remboursable(m.getRemboursable())
                .description(m.getDescription())
                .build();
    }
}