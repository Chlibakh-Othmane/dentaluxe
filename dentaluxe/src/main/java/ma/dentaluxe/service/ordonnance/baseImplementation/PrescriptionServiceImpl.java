package ma.dentaluxe.service.ordonnance.baseImplementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.dto.ordonnance.*;
import ma.dentaluxe.service.ordonnance.api.PrescriptionService;
import ma.dentaluxe.repository.modules.ordonnance.api.PrescriptionRepository;
import ma.dentaluxe.repository.modules.ordonnance.api.OrdonnanceRepository;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.entities.ordonnance.Ordonnance;

// === IMPORT DES DTO (Packages séparés) ===

// === IMPORT DES EXCEPTIONS (Packages séparés) ===
import ma.dentaluxe.common.exceptions.modules.ordonnance.PrescriptionNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation rectifiée du service Prescription.
 * Utilise les DTO et Exceptions externes.
 */
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final OrdonnanceRepository ordonnanceRepository;

    // Constructeur vide pour ApplicationContext
    public PrescriptionServiceImpl() {
        this.prescriptionRepository = (PrescriptionRepository) ApplicationContext.getBean("prescriptionRepo");
        this.ordonnanceRepository = (OrdonnanceRepository) ApplicationContext.getBean("ordonnanceRepo");
    }

    @Override
    public PrescriptionDTO createPrescription(PrescriptionCreateDTO dto) {
        // Vérification de l'ordonnance parente
        if (ordonnanceRepository.findById(dto.getIdOrdo()) == null) {
            throw new RuntimeException("Ordonnance parente introuvable.");
        }

        Prescription entity = new Prescription();
        entity.setIdOrdo(dto.getIdOrdo());
        entity.setIdMedicament(dto.getIdMedicament());
        entity.setQuantite(dto.getQuantite());
        entity.setFrequence(dto.getFrequence());
        entity.setDureeEnJours(dto.getDureeEnJours());

        prescriptionRepository.create(entity);
        return convertToPrescriptionDTO(entity);
    }

    @Override
    public PrescriptionDTO getPrescriptionById(Long id) {
        Prescription p = prescriptionRepository.findById(id);
        if (p == null) throw new PrescriptionNotFoundException(id);
        return convertToPrescriptionDTO(p);
    }

    /**
     * RECTIFICATION : Cette méthode accepte maintenant 1 seul argument (le DTO)
     * au lieu de 4 arguments séparés.
     */
    @Override
    public PrescriptionDTO updatePrescription(PrescriptionUpdateDTO dto) {
        // 1. On cherche la ligne existante
        Prescription p = prescriptionRepository.findById(dto.getIdPrescription());
        if (p == null) {
            throw new PrescriptionNotFoundException(dto.getIdPrescription());
        }

        // 2. Mise à jour des champs à partir du DTO
        p.setQuantite(dto.getQuantite());
        p.setFrequence(dto.getFrequence());
        p.setDureeEnJours(dto.getDureeEnJours());

        // 3. Sauvegarde
        prescriptionRepository.update(p);

        // 4. Retour du résultat converti
        return convertToPrescriptionDTO(p);
    }

    @Override
    public void deletePrescription(Long id) {
        if (prescriptionRepository.findById(id) == null) {
            throw new PrescriptionNotFoundException(id);
        }
        prescriptionRepository.deleteById(id);
    }

    @Override
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(this::convertToPrescriptionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByOrdonnance(Long idOrdo) {
        return prescriptionRepository.findByOrdonnance(idOrdo).stream()
                .map(this::convertToPrescriptionDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByMedicament(Long idMedicament) {
        return List.of();
    }

    @Override
    public List<PrescriptionDTO> searchByFrequence(String frequence) {
        return List.of();
    }

    @Override
    public List<PrescriptionDTO> searchByDuree(int minJours, int maxJours) {
        return List.of();
    }

    @Override
    public int getQuantiteTotalePrescrite(Long idMedicament) {
        return 0;
    }

    @Override
    public boolean isMedicamentPrescritDansOrdonnance(Long idOrdo, Long idMedicament) {
        return false;
    }

    @Override
    public List<MedicamentPrescriptionDTO> getMedicamentsLesPlusPrescrits(int limit) {
        return List.of();
    }

    @Override
    public PrescriptionReportDTO generatePrescriptionReport(Long idOrdo) {
        return null;
    }

    @Override
    public ValidationResultDTO validatePrescription(PrescriptionCreateDTO prescriptionDTO) {
        return null;
    }

    @Override
    public LocalDate calculateDateFinTraitement(Long idPrescription) {
        return null;
    }

    // ==================== MAPPING PRIVÉ ====================

    private PrescriptionDTO convertToPrescriptionDTO(Prescription p) {
        return PrescriptionDTO.builder()
                .idPrescription(p.getIdPrescription())
                .idOrdo(p.getIdOrdo())
                .idMedicament(p.getIdMedicament())
                .quantite(p.getQuantite())
                .frequence(p.getFrequence())
                .dureeEnJours(p.getDureeEnJours())
                .nomMedicament("Médicament ID: " + p.getIdMedicament()) // Sera enrichi par le controller
                .build();
    }
}