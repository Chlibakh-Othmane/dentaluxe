package ma.dentaluxe.mvc.controllers.modules.patient.batch_implentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.patient.api.AntecedentPatientController;
import ma.dentaluxe.service.patient.api.AntecedentPatientService;
import ma.dentaluxe.mvc.dto.patient.*;
import java.util.List;

public class AntecedentPatientBatchController implements AntecedentPatientController {
    private final AntecedentPatientService service = (AntecedentPatientService) ApplicationContext.getBean("antecedentPatientService");

    @Override
    public void linkToPatient(AntecedentPatientCreateDTO dto) {
        System.out.println("Batch : Liaison Patient ID " + dto.getIdPatient() + " avec Antécédent ID " + dto.getIdAntecedent());
        service.addAntecedentToPatient(dto);
    }

    @Override public List<AntecedentPatientDTO> getPatientMedicalHistory(Long pId) { return service.getPatientMedicalHistory(pId); }
    @Override public void updateNote(AntecedentPatientDTO dto) { service.updatePatientAntecedent(dto); }
}