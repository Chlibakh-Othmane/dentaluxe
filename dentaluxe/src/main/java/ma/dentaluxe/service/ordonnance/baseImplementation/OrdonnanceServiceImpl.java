package ma.dentaluxe.service.ordonnance.baseImplementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.service.ordonnance.api.OrdonnanceService;
import ma.dentaluxe.repository.modules.ordonnance.api.OrdonnanceRepository;
import ma.dentaluxe.repository.modules.ordonnance.api.PrescriptionRepository;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.entities.ordonnance.Prescription;

// === IMPORT DES DTO (Packages séparés) ===
import ma.dentaluxe.mvc.dto.ordonnance.*;

// === IMPORT DES EXCEPTIONS (Packages séparés) ===
import ma.dentaluxe.common.exceptions.modules.ordonnance.OrdonnanceNotFoundException;
import ma.dentaluxe.common.exceptions.modules.ordonnance.PrescriptionNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrdonnanceServiceImpl implements OrdonnanceService {

    private final OrdonnanceRepository ordonnanceRepo;
    private final PrescriptionRepository prescriptionRepo;

    public OrdonnanceServiceImpl() {
        this.ordonnanceRepo = (OrdonnanceRepository) ApplicationContext.getBean("ordonnanceRepo");
        this.prescriptionRepo = (PrescriptionRepository) ApplicationContext.getBean("prescriptionRepo");
    }

    // ==================== GESTION DES ORDONNANCES ====================

    @Override
    public OrdonnanceDTO createOrdonnance(OrdonnanceCreateDTO dto, List<PrescriptionCreateDTO> prescriptions) {
        // 1. Création de l'entité Ordonnance
        Ordonnance ordonnance = new Ordonnance();
        ordonnance.setIdDM(dto.getIdDM());
        ordonnance.setIdMedecin(dto.getIdMedecin());
        ordonnance.setDateOrdonnance(dto.getDateOrdonnance() != null ? dto.getDateOrdonnance() : LocalDate.now());

        // 2. Sauvegarde de l'ordonnance (génère l'ID en base)
        ordonnanceRepo.create(ordonnance);

        // --- VÉRIFICATION CRITIQUE ---
        if (ordonnance.getIdOrdo() == null) {
            throw new RuntimeException("ERREUR CRITIQUE : L'ordonnance n'a pas pu être créée. Vérifiez que l'ID Médecin (ID: "+dto.getIdMedecin()+") et l'ID Dossier existent en base.");
        }

        // 3. Boucle pour créer chaque prescription liée à cette ordonnance
        List<PrescriptionDTO> prescriptionsCreees = new ArrayList<>();

        if (prescriptions != null) {
            for (PrescriptionCreateDTO pDto : prescriptions) {
                Prescription p = new Prescription();
                p.setIdOrdo(ordonnance.getIdOrdo()); // Utilise l'ID qu'on vient de générer
                p.setIdMedicament(pDto.getIdMedicament());
                p.setQuantite(pDto.getQuantite());
                p.setFrequence(pDto.getFrequence());
                p.setDureeEnJours(pDto.getDureeEnJours());

                prescriptionRepo.create(p);
                prescriptionsCreees.add(convertToPrescriptionDTO(p));
            }
        }

        // 4. Retourner le DTO complet
        OrdonnanceDTO resultDTO = convertToOrdonnanceDTO(ordonnance);
        resultDTO.setPrescriptions(prescriptionsCreees);
        return resultDTO;
    }

    @Override
    public OrdonnanceDTO getOrdonnanceById(Long id) {
        Ordonnance o = ordonnanceRepo.findById(id);
        if (o == null) throw new OrdonnanceNotFoundException(id);

        List<Prescription> prescriptions = prescriptionRepo.findByOrdonnance(id);
        OrdonnanceDTO dto = convertToOrdonnanceDTO(o);
        dto.setPrescriptions(prescriptions.stream()
                .map(this::convertToPrescriptionDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    public OrdonnanceDTO updateOrdonnance(Long id, OrdonnanceUpdateDTO dto) {
        Ordonnance o = ordonnanceRepo.findById(id);
        if (o == null) throw new OrdonnanceNotFoundException(id);

        o.setDateOrdonnance(dto.getDateOrdonnance());
        ordonnanceRepo.update(o);

        return getOrdonnanceById(id);
    }

    @Override
    public void deleteOrdonnance(Long id) {
        // Suppression "Propre" : d'abord les lignes de prescriptions (enfant), puis l'ordonnance (parent)
        prescriptionRepo.deleteByOrdonnance(id);
        ordonnanceRepo.deleteById(id);
    }

    @Override
    public List<OrdonnanceDTO> getAllOrdonnances() {
        return ordonnanceRepo.findAll().stream()
                .map(this::convertToOrdonnanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdonnanceDTO> getOrdonnancesByDossierMedical(Long idDM) {
        return ordonnanceRepo.findByDossierMedical(idDM).stream()
                .map(this::convertToOrdonnanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdonnanceDTO> getOrdonnancesByMedecin(Long idMedecin) {
        return ordonnanceRepo.findByMedecin(idMedecin).stream()
                .map(this::convertToOrdonnanceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrdonnanceDTO> searchOrdonnancesByDateRange(LocalDate dateDebut, LocalDate dateFin) {
        return ordonnanceRepo.findAll().stream()
                .filter(o -> !o.getDateOrdonnance().isBefore(dateDebut) && !o.getDateOrdonnance().isAfter(dateFin))
                .map(this::convertToOrdonnanceDTO)
                .collect(Collectors.toList());
    }

    // ==================== GESTION DES PRESCRIPTIONS ====================

    @Override
    public PrescriptionDTO addPrescriptionToOrdonnance(Long idOrdo, PrescriptionCreateDTO pDto) {
        Prescription p = new Prescription();
        p.setIdOrdo(idOrdo);
        p.setIdMedicament(pDto.getIdMedicament());
        p.setQuantite(pDto.getQuantite());
        p.setFrequence(pDto.getFrequence());
        p.setDureeEnJours(pDto.getDureeEnJours());

        prescriptionRepo.create(p);
        return convertToPrescriptionDTO(p);
    }

    @Override
    public PrescriptionDTO updatePrescription(Long id, Integer qte, String freq, Integer duree) {
        Prescription p = prescriptionRepo.findById(id);
        if (p == null) throw new PrescriptionNotFoundException(id);

        p.setQuantite(qte);
        p.setFrequence(freq);
        p.setDureeEnJours(duree);

        prescriptionRepo.update(p);
        return convertToPrescriptionDTO(p);
    }

    @Override
    public void deletePrescription(Long id) {
        prescriptionRepo.deleteById(id);
    }

    @Override
    public List<PrescriptionDTO> getPrescriptionsByOrdonnance(Long idOrdo) {
        return prescriptionRepo.findByOrdonnance(idOrdo).stream()
                .map(this::convertToPrescriptionDTO)
                .collect(Collectors.toList());
    }

    // ==================== FONCTIONNALITÉS AVANCÉES (SIMULÉES) ====================

    @Override
    public String generateOrdonnancePDF(Long idOrdo) {
        return "exports/pdf/ordonnance_" + idOrdo + ".pdf";
    }

    @Override
    public List<String> checkMedicationInteractions(Long idOrdo) {
        return List.of("Aucune interaction majeure détectée.");
    }

    @Override
    public OrdonnanceDTO duplicateOrdonnance(Long idOrdo, LocalDate nouvelleDate) {
        OrdonnanceDTO original = getOrdonnanceById(idOrdo);

        OrdonnanceCreateDTO createDTO = OrdonnanceCreateDTO.builder()
                .idDM(original.getIdDM())
                .idMedecin(original.getIdMedecin())
                .dateOrdonnance(nouvelleDate)
                .build();

        List<PrescriptionCreateDTO> prescriptions = original.getPrescriptions().stream().map(p ->
                PrescriptionCreateDTO.builder()
                        .idMedicament(p.getIdMedicament())
                        .quantite(p.getQuantite())
                        .frequence(p.getFrequence())
                        .dureeEnJours(p.getDureeEnJours())
                        .posologie("")
                        .build()
        ).collect(Collectors.toList());

        return createOrdonnance(createDTO, prescriptions);
    }

    @Override
    public OrdonnanceStatsDTO getOrdonnanceStatistics() {
        return OrdonnanceStatsDTO.builder()
                .totalOrdonnances(ordonnanceRepo.findAll().size())
                .ordonnancesCeMois(5)
                .moyenneMedicamentsParOrdonnance(3.5)
                .build();
    }

    // ==================== MAPPING PRIVÉ ====================

    private OrdonnanceDTO convertToOrdonnanceDTO(Ordonnance o) {
        return OrdonnanceDTO.builder()
                .idOrdo(o.getIdOrdo())
                .idDM(o.getIdDM())
                .idMedecin(o.getIdMedecin())
                .dateOrdonnance(o.getDateOrdonnance())
                .nomMedecin("Dr. ID: " + o.getIdMedecin())
                .build();
    }

    private PrescriptionDTO convertToPrescriptionDTO(Prescription p) {
        return PrescriptionDTO.builder()
                .idPrescription(p.getIdPrescription())
                .idMedicament(p.getIdMedicament())
                .quantite(p.getQuantite())
                .frequence(p.getFrequence())
                .dureeEnJours(p.getDureeEnJours())
                .nomMedicament("Médicament ID: " + p.getIdMedicament())
                .build();
    }
}