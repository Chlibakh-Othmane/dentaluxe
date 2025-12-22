package ma.dentaluxe.service.patient.baseImplimentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.service.patient.api.AntecedentService;
import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.repository.modules.patient.api.AntecedentRepository;

// NOUVEAUX IMPORTS
import ma.dentaluxe.mvc.dto.antecedent.AntecedentDTO;
import ma.dentaluxe.common.exceptions.modules.patient.AntecedentNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion du CATALOGUE des antécédents.
 */
public class AntecedentServiceImpl implements AntecedentService {

    private final AntecedentRepository antecedentRepository;

    public AntecedentServiceImpl() {
        // Récupération via le Singleton de l'ApplicationContext
        this.antecedentRepository = (AntecedentRepository) ApplicationContext.getBean("antecedentRepo");
    }

    @Override
    public List<AntecedentDTO> findAll() {
        return antecedentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()).reversed();
    }

    @Override
    public AntecedentDTO findById(Long id) {
        Antecedent ant = antecedentRepository.findById(id);
        if (ant == null) {
            throw new AntecedentNotFoundException(id.toString());
        }
        return convertToDTO(ant);
    }

    @Override
    public Antecedent create(Antecedent antecedent) {
        return null;
    }

    @Override
    public Antecedent update(Long id, Antecedent antecedent) {
        return null;
    }

    @Override
    public AntecedentDTO create(AntecedentDTO dto) {
        // ... validation ...
        Antecedent entity = Antecedent.builder()
                .nom(dto.getNom())
                .categorie(dto.getCategorie())
                .niveauRisque(dto.getNiveauRisque())
                .build();

        // Le repo remplit l'id dans l'objet 'entity'
        antecedentRepository.create(entity);

        // On recopie l'id dans le DTO
        dto.setId(entity.getId());

        return dto;
    }

    @Override
    public void delete(Long id) {
        if (antecedentRepository.findById(id) == null) {
            throw new AntecedentNotFoundException(id.toString());
        }
        antecedentRepository.deleteById(id);
    }

    @Override
    public List<Antecedent> findByNom(String nom) {
        return List.of();
    }

    @Override
    public List<AntecedentDTO> findByCategorie(CategorieAntecedent cat) {
        return antecedentRepository.findByCategorie(cat).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()).reversed();
    }

    @Override
    public List<Antecedent> findByNiveauRisque(NiveauRisque niveauRisque) {
        return List.of();
    }

    @Override
    public List<Antecedent> findByPatientId(Long patientId) {
        return List.of();
    }

    @Override
    public void addAntecedentToPatient(Long patientId, Long antecedentId, String notes) {

    }

    @Override
    public void removeAntecedentFromPatient(Long patientId, Long antecedentId) {

    }

    @Override
    public String getNotesForPatient(Long patientId, Long antecedentId) {
        return "";
    }

    @Override
    public void updateNotesForPatient(Long patientId, Long antecedentId, String notes) {

    }

    @Override
    public String getStatistics() {
        return "";
    }

    @Override
    public List<Antecedent> findWithPagination(int page, int size) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }

    // ==================== MÉTHODE DE CONVERSION ====================

    private AntecedentDTO convertToDTO(Antecedent ant) {
        return AntecedentDTO.builder()
                .id(ant.getId())
                .nom(ant.getNom())
                .categorie(ant.getCategorie())
                .niveauRisque(ant.getNiveauRisque())
                .build();
    }

    // Note : Les méthodes liées au Patient (addAntecedentToPatient, etc.)
    // sont maintenant gérées par AntecedentPatientServiceImpl pour plus de clarté.
}