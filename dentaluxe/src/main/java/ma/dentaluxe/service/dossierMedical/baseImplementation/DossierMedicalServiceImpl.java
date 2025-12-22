package ma.dentaluxe.service.dossierMedical.baseImplementation;

import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.dossier.Antecedents;
import ma.dentaluxe.entities.dossier.DossierMedical;
// 👇 IMPORT IMPORTANT POUR CORRIGER L'ERREUR 'EN_ATTENTE'
import ma.dentaluxe.entities.enums.StatutConsultation;
import ma.dentaluxe.mvc.dto.dossier.*;
import ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation.*;
import ma.dentaluxe.service.dossierMedical.api.DossierMedicalService;
import ma.dentaluxe.conf.ApplicationContext; // AJOUTÉ
import ma.dentaluxe.repository.modules.dossierMedical.api.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class DossierMedicalServiceImpl implements DossierMedicalService {

    private final DossierMedicalRepositoryImpl dossierRepo = new DossierMedicalRepositoryImpl();
    private final AntecedentsRepositoryImpl antecedentRepo = new AntecedentsRepositoryImpl();
    private final ConsultationRepositoryImpl consultationRepo = new ConsultationRepositoryImpl();

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
        Antecedents entity = new Antecedents();
        entity.setIdDM(dto.getIdDM());
        entity.setNom(dto.getNom());
        entity.setCategorie(dto.getCategorie());
        entity.setNiveauDeRisque(dto.getNiveauDeRisque());

        antecedentRepo.create(entity);
        dto.setIdAntecedent(entity.getIdAntecedent());
        return dto;
    }

    @Override
    public List<AntecedentDTO> getAntecedentsByDossier(Long idDM) {
        return antecedentRepo.findByDossierMedicalId(idDM).stream()
                .map(a -> new AntecedentDTO(
                        a.getIdAntecedent(), a.getIdDM(), a.getNom(),
                        a.getCategorie(), a.getNiveauDeRisque()))
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
        // 👇 ICI CELA NE DEVRAIT PLUS ÊTRE ROUGE GRÂCE À L'IMPORT
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

    // --- MAPPERS CORRIGÉS ---

    private DossierMedicalDTO mapToDTO(DossierMedical dm) {
        if (dm == null) return null;
        return new DossierMedicalDTO(dm.getIdDM(), dm.getIdPatient(), dm.getDateDeCreation());
    }

    // 👇 CORRECTION MAJEURE DU CONSTRUCTEUR
    private ConsultationDTO mapToConsultationDTO(Consultation c) {
        if (c == null) return null;
        return new ConsultationDTO(
                c.getIdConsultation(),
                c.getIdDM(),
                c.getIdRDV(),
                c.getIdMedecin(),
                c.getDateConsultation(),
                null, // <--- AJOUT DE 'null' POUR LE CHAMP 'MOTIF' (si l'entité ne l'a pas)
                c.getStatut(),
                c.getObservation()
        );
    }
}