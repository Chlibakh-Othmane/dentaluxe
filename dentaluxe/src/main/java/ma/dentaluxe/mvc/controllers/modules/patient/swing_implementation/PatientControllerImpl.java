package ma.dentaluxe.mvc.controllers.modules.patient.batch_implentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.patient.api.PatientController;
import ma.dentaluxe.service.patient.api.PatientService;
import ma.dentaluxe.mvc.dto.patient.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class PatientControllerImpl implements PatientController {
    private final PatientService patientService = (PatientService) ApplicationContext.getBean("patientService");

    @Override
    public void showRecentPatients() {
        System.out.println("\n>>> RAPPORT BATCH : PATIENTS CRÉÉS LE " + LocalDate.now());
        patientService.getAllPatients().stream()
                .filter(p -> p.getDateCreation() != null && p.getDateCreation().toLocalDate().isEqual(LocalDate.now()))
                .sorted(Comparator.comparing(PatientDTO::getDateCreation))
                .forEach(p -> System.out.printf("[%s] %s - %d ans\n",
                        p.getDateCreationFormatee(), p.getNomComplet(), p.getAge()));
    }

    @Override public void savePatient(PatientCreateDTO dto) { patientService.createPatient(dto); }
    @Override public List<PatientDTO> getAllPatients() { return patientService.getAllPatients(); }
    @Override public void removePatient(Long id) { patientService.deletePatient(id); }
}