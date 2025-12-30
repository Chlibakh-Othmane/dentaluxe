package ma.dentaluxe.mvc.controllers.modules.ordonnance.batch_implentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.ordonnance.api.PrescriptionController;
import ma.dentaluxe.service.ordonnance.api.PrescriptionService;
import ma.dentaluxe.mvc.dto.ordonnance.*;
import java.util.List;

public class PrescriptionBatchController implements PrescriptionController {

    private final PrescriptionService service = (PrescriptionService) ApplicationContext.getBean("prescriptionService");

    @Override
    public void addPrescriptionToOrdo(PrescriptionCreateDTO dto) {
        System.out.println("BATCH : Ajout médicament ID " + dto.getIdMedicament() + " à l'Ordo " + dto.getIdOrdo());
        service.createPrescription(dto);
    }

    @Override public void updatePrescriptionDetails(Long id, Integer qte, String f, Integer d) { PrescriptionUpdateDTO updateDTO = PrescriptionUpdateDTO.builder()
            .idPrescription(id)
            .quantite(qte)
            .frequence(f)
            .dureeEnJours(d)
            .build();

        service.updatePrescription(updateDTO); }
    @Override public void removePrescription(Long id) { service.deletePrescription(id); }
    @Override public List<PrescriptionDTO> getLinesByOrdonnance(Long idOrdo) { return service.getPrescriptionsByOrdonnance(idOrdo); }
}