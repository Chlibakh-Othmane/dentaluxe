package ma.dentaluxe.service.dossierMedical.baseImplementation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.patient.Antecedent; // Utilise TON entité unifiée
import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.entities.enums.StatutConsultation;
import ma.dentaluxe.mvc.dto.dossier.*;
import ma.dentaluxe.repository.modules.dossierMedical.api.*;
import ma.dentaluxe.service.dossierMedical.api.DossierMedicalService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DossierMedicalServiceImpl implements DossierMedicalService {

    // On déclare les interfaces (le contrat)
    private final DossierMedicalRepository dossierRepo;
    private final AntecedentsRepository antecedentRepo;
    private final ConsultationRepository consultationRepo;

    // CONSTRUCTEUR VIDE : On récupère les instances via l'ApplicationContext
    public DossierMedicalServiceImpl() {
        this.dossierRepo = (DossierMedicalRepository) ApplicationContext.getBean("dossierRepo");
        this.antecedentRepo = (AntecedentsRepository) ApplicationContext.getBean("antecedentsRepo");
        this.consultationRepo = (ConsultationRepository) ApplicationContext.getBean("consultationRepo");
    }

    @Override
    public DossierMedicalDTO createDossier(Long idPatient) {
        DossierMedical dm = new DossierMedical();
        dm.setIdPatient(idPatient);
        dm.setDateDeCreation(LocalDate.now());
        dossierRepo.create(dm);
        return mapToDTO(dm);
    }

    @Override
    public DossierMedicalDTO getDossierById(Long id) {
        return mapToDTO(dossierRepo.findById(id));
    }

    @Override
    public DossierMedicalDTO getDossierByPatientId(Long idPatient) {
        return mapToDTO(dossierRepo.findByPatientId(idPatient));
    }

    @Override
    public boolean hasDossier(Long idPatient) {
        return dossierRepo.findByPatientId(idPatient) != null;
    }

    @Override
    public AntecedentDTO addAntecedent(AntecedentDTO dto) {
        // On utilise l'entité Antecedent (sans S) que nous avons unifiée
        Antecedent entity = Antecedent.builder()
                .nom(dto.getNom())
                .categorie(dto.getCategorie())
                .niveauRisque(dto.getNiveauDeRisque())
                .build();

        antecedentRepo.create(entity);
        dto.setIdAntecedent(entity.getId()); // On récupère l'ID généré
        return dto;
    }

    @Override
    public List<AntecedentDTO> getAntecedentsByDossier(Long idDM) {
        return antecedentRepo.findByDossierMedicalId(idDM).stream()
                .map(a -> new AntecedentDTO(
                        a.getId(), a.getIdDM(), a.getNom(),
                        a.getCategorie(), a.getNiveauRisque()))
                .collect(Collectors.toList());
    }

    @Override
    public void removeAntecedent(Long idAntecedent) {
        antecedentRepo.deleteById(idAntecedent);
    }

    @Override
    public ConsultationDTO planifierConsultation(ConsultationDTO dto) {
        Consultation entity = new Consultation();
        entity.setIdDM(dto.getIdDM());
        entity.setIdRDV(dto.getIdRDV());
        entity.setIdMedecin(dto.getIdMedecin());
        entity.setDateConsultation(dto.getDateConsultation());
        entity.setStatut(StatutConsultation.PLANIFIEE);
        entity.setObservation(dto.getObservation());

        consultationRepo.create(entity);
        return mapToConsultationDTO(entity);
    }

    @Override
    public ConsultationDTO terminerConsultation(Long idConsultation, String observation) {
        Consultation c = consultationRepo.findById(idConsultation);
        if (c != null) {
            c.setStatut(StatutConsultation.TERMINEE);
            c.setObservation(observation);
            consultationRepo.update(c);
        }
        return mapToConsultationDTO(c);
    }

    @Override
    public void annulerConsultation(Long idConsultation) {
        Consultation c = consultationRepo.findById(idConsultation);
        if(c != null) {
            c.setStatut(StatutConsultation.ANNULEE);
            consultationRepo.update(c);
        }
    }

    @Override
    public ConsultationDTO getConsultationById(Long id) {
        return mapToConsultationDTO(consultationRepo.findById(id));
    }

    @Override
    public List<ConsultationDTO> getConsultationsByDossier(Long idDM) {
        return consultationRepo.findByDossierMedicalId(idDM).stream()
                .map(this::mapToConsultationDTO)
                .collect(Collectors.toList());
    }

    private DossierMedicalDTO mapToDTO(DossierMedical dm) {
        if (dm == null) return null;
        return new DossierMedicalDTO(dm.getIdDM(), dm.getIdPatient(), dm.getDateDeCreation());
    }

    private ConsultationDTO mapToConsultationDTO(Consultation c) {
        if (c == null) return null;
        return new ConsultationDTO(
                c.getIdConsultation(),
                c.getIdDM(),
                c.getIdRDV(),
                c.getIdMedecin(),
                c.getDateConsultation(),
                null,
                c.getStatut(),
                c.getObservation()
        );
    }
}