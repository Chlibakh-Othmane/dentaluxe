package ma.dentaluxe.service.patient.baseImplimentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.repository.modules.patient.api.PatientRepository;
import ma.dentaluxe.service.patient.api.PatientService;
import ma.dentaluxe.mvc.dto.patient.*;
import ma.dentaluxe.common.exceptions.modules.patient.PatientNotFoundException;
import ma.dentaluxe.common.exceptions.modules.patient.DuplicateEmailException;
import ma.dentaluxe.common.utilitaire.DateUtils;
import ma.dentaluxe.common.utilitaire.ValidationUtils;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl() {
        this.patientRepository = (PatientRepository) ApplicationContext.getBean("patientRepo");
    }

    @Override
    public PatientDTO createPatient(PatientCreateDTO dto) {
        // --- RECTIFICATION : Validation via Utils ---
        if (!ValidationUtils.isValidEmail(dto.getEmail())) {
            throw new RuntimeException("Format d'email invalide.");
        }
        if (!ValidationUtils.isValidPhone(dto.getTelephone())) {
            throw new RuntimeException("Format de téléphone marocain invalide.");
        }

        if (emailExists(dto.getEmail())) throw new DuplicateEmailException(dto.getEmail());

        Patient p = Patient.builder()
                .nom(dto.getNom()).prenom(dto.getPrenom())
                .telephone(dto.getTelephone()).email(dto.getEmail())
                .dateNaissance(dto.getDateNaissance())
                .sexe(dto.getSexe()).assurance(dto.getAssurance())
                .build();

        patientRepository.create(p);
        return getPatientById(p.getId());
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        Patient p = patientRepository.findById(id);
        if (p == null) throw new PatientNotFoundException(id);
        return convertToPatientDTO(p);
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientUpdateDTO dto) {
        Patient p = patientRepository.findById(id);
        if (p == null) throw new PatientNotFoundException(id);
        p.setNom(dto.getNom()); p.setPrenom(dto.getPrenom());
        p.setTelephone(dto.getTelephone()); p.setEmail(dto.getEmail());
        p.setSexe(dto.getSexe()); p.setAssurance(dto.getAssurance());
        patientRepository.update(p);
        return convertToPatientDTO(p);
    }

    @Override
    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAll().stream().map(this::convertToPatientDTO).collect(Collectors.toList());
    }

    // --- MÉTHODES DE RECHERCHE (Obligatoires pour enlever le rouge) ---

    @Override
    public List<PatientDTO> searchByNom(String nom) {
        return patientRepository.findByNom(nom).stream().map(this::convertToPatientDTO).collect(Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchByPrenom(String prenom) {
        // On filtre manuellement si le repo n'a pas la méthode
        return getAllPatients().stream()
                .filter(p -> p.getPrenom().toLowerCase().contains(prenom.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientDTO> searchByTelephone(String tel) {
        return getAllPatients().stream()
                .filter(p -> p.getTelephone().contains(tel))
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO searchByEmail(String email) {
        return getAllPatients().stream()
                .filter(p -> p.getEmail().equalsIgnoreCase(email))
                .findFirst().orElse(null);
    }

    @Override
    public boolean emailExists(String email) {
        return searchByEmail(email) != null;
    }



    private PatientDTO convertToPatientDTO(Patient p) {
        return PatientDTO.builder()
                .id(p.getId())
                .nom(p.getNom())
                .prenom(p.getPrenom())
                .email(p.getEmail())
                .telephone(p.getTelephone())
                // Utilisation de DateUtils pour l'âge
                .age(DateUtils.calculateAge(p.getDateNaissance()))
                .sexe(p.getSexe())
                .assurance(p.getAssurance())
                .build();
    }
}