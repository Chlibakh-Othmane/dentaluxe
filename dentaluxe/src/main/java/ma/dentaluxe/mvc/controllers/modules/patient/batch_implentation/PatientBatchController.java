package ma.dentaluxe.mvc.controllers.modules.patient.batch_implentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.patient.api.PatientController;
import ma.dentaluxe.mvc.dto.patient.PatientCreateDTO;
import ma.dentaluxe.service.patient.api.PatientService;
import ma.dentaluxe.mvc.dto.patient.PatientDTO;
import java.time.LocalDate;
import java.util.List;

public class PatientBatchController implements PatientController {

    private final PatientService patientService;

    public PatientBatchController() {
        this.patientService = (PatientService) ApplicationContext.getBean("patientService");
    }

    @Override
    public void showRecentPatients() {
        System.out.println("LOG BATCH - Patients du " + LocalDate.now());
        patientService.getAllPatients().stream()
                .filter(p -> p.getDateCreation() != null && p.getDateCreation().toLocalDate().isEqual(LocalDate.now()))
                .forEach(p -> System.out.println("ID: " + p.getId() + " | Nom: " + p.getNomComplet()));
    }

    @Override
    public void savePatient(PatientCreateDTO dto) {

    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return List.of();
    }

    @Override
    public void removePatient(Long id) {

    }
}