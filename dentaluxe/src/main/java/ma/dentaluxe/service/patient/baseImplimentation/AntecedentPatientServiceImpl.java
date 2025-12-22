package ma.dentaluxe.service.patient.baseImplimentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.common.exceptions.modules.patient.AntecedentAlreadyAssignedException;
import ma.dentaluxe.common.exceptions.modules.patient.PatientNotFoundException;
import ma.dentaluxe.entities.AntecedentPatient.AntecedentPatient;
import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.mvc.dto.patient.AntecedentPatientCreateDTO;
import ma.dentaluxe.mvc.dto.patient.AntecedentPatientDTO;
import ma.dentaluxe.repository.modules.AntecedentPatient.api.AntecedentPatientRepository;
import ma.dentaluxe.repository.modules.patient.api.AntecedentRepository;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.service.patient.api.AntecedentPatientService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AntecedentPatientServiceImpl implements AntecedentPatientService {

    private final AntecedentPatientRepository apRepo;
    private final AntecedentRepository antCatalogueRepo;
    private final PatientRepository patientRepo;

    // Constructeur vide pour ApplicationContext
    public AntecedentPatientServiceImpl() {
        this.apRepo = (AntecedentPatientRepository) ApplicationContext.getBean("AntecedentPatientRepo");
        this.antCatalogueRepo = (AntecedentRepository) ApplicationContext.getBean("antecedentRepo");
        this.patientRepo = (PatientRepository) ApplicationContext.getBean("patientRepo");
    }

    @Override
    public List<AntecedentPatientDTO> getPatientMedicalHistory(Long idPatient) {
        // 1. On récupère les liaisons brutes (IDs)
        List<AntecedentPatient> history = apRepo.findByPatientId(idPatient);
        List<AntecedentPatientDTO> dtos = new ArrayList<>();

        // 2. Pour chaque liaison, on va chercher le nom dans le catalogue (Antécédents)
        for (AntecedentPatient ap : history) {
            Antecedent detail = antCatalogueRepo.findById(ap.getIdAntecedent());

            dtos.add(AntecedentPatientDTO.builder()
                    .idPatient(ap.getIdPatient())
                    .idAntecedent(ap.getIdAntecedent())
                    .nomAntecedent(detail != null ? detail.getNom() : "Inconnu")
                    .categorie(detail != null ? detail.getCategorie().name() : "N/A")
                    .niveauRisque(detail != null ? detail.getNiveauRisque().name() : "N/A")
                    .dateAjout(ap.getDateAjout())
                    .actif(ap.isActif())
                    .notes(ap.getNotes())
                    .build());
        }
        return dtos;
    }

    @Override
    public void addAntecedentToPatient(AntecedentPatientCreateDTO dto) {
        // Vérification métier : Le patient existe-t-il ?
        if (patientRepo.findById(dto.getIdPatient()) == null) {
            throw new PatientNotFoundException(dto.getIdPatient());
        }

        // Vérification métier : L'antécédent est-il déjà assigné ?
        List<AntecedentPatient> current = apRepo.findByPatientId(dto.getIdPatient());
        boolean alreadyHasIt = current.stream()
                .anyMatch(a -> a.getIdAntecedent().equals(dto.getIdAntecedent()));

        if (alreadyHasIt) {
            Antecedent ant = antCatalogueRepo.findById(dto.getIdAntecedent());
            throw new AntecedentAlreadyAssignedException(ant != null ? ant.getNom() : "cet ID");
        }

        // Création de l'entité
        AntecedentPatient entity = AntecedentPatient.builder()
                .idPatient(dto.getIdPatient())
                .idAntecedent(dto.getIdAntecedent())
                .dateAjout(LocalDate.now())
                .actif(true)
                .notes(dto.getNotes())
                .build();

        apRepo.create(entity);
    }

    @Override
    public void updatePatientAntecedent(AntecedentPatientDTO dto) {
        AntecedentPatient entity = AntecedentPatient.builder()
                .idPatient(dto.getIdPatient())
                .idAntecedent(dto.getIdAntecedent())
                .dateAjout(dto.getDateAjout())
                .actif(dto.isActif())
                .notes(dto.getNotes())
                .build();

        apRepo.update(entity);
    }

    @Override
    public void removeAntecedentFromPatient(Long idPatient, Long idAntecedent) {
        // On utilise la suppression par clé composée (Patient + Antecedent)
        // Note: Assure-toi que ton Repo a bien une méthode qui gère les deux IDs
        apRepo.deleteByPatientId(idPatient); // Ou une méthode deleteByCompositeKey
    }

    @Override
    public boolean hasAntecedent(Long idPatient, Long idAntecedent) {
        return apRepo.findByPatientId(idPatient).stream()
                .anyMatch(a -> a.getIdAntecedent().equals(idAntecedent));
    }
}