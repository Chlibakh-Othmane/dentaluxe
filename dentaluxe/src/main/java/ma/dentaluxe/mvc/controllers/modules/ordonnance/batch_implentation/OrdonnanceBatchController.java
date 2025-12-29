package ma.dentaluxe.mvc.controllers.modules.ordonnance.batch_implentation;

import ma.dentaluxe.conf.ApplicationContext;
import ma.dentaluxe.mvc.controllers.modules.ordonnance.api.OrdonnanceController;
import ma.dentaluxe.service.ordonnance.api.OrdonnanceService;
import ma.dentaluxe.mvc.dto.ordonnance.*;
import java.util.List;

public class OrdonnanceBatchController implements OrdonnanceController {

    private final OrdonnanceService service = (OrdonnanceService) ApplicationContext.getBean("ordonnanceService");

    @Override
    public void saveOrdonnance(OrdonnanceCreateDTO ordoDto, List<PrescriptionCreateDTO> prescriptions) {
        System.out.println("BATCH : Création d'une ordonnance pour le Dossier " + ordoDto.getIdDM());
        service.createOrdonnance(ordoDto, prescriptions);
    }

    @Override
    public List<OrdonnanceDTO> getHistoryByDossier(Long idDM) {
        return service.getOrdonnancesByDossierMedical(idDM);
    }

    @Override
    public void deleteOrdonnance(Long idOrdo) {
        System.out.println("BATCH : Suppression Ordonnance ID " + idOrdo);
        service.deleteOrdonnance(idOrdo);
    }

    @Override
    public void printOrdonnance(Long idOrdo) {
        System.out.println("BATCH : Export PDF de l'ordonnance " + idOrdo);
    }
}