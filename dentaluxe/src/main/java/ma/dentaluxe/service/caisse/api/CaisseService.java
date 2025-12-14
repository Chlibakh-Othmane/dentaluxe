package ma.dentaluxe.service.caisse.api;

import ma.dentaluxe.mvc.dto.caisse.*; // Assure-toi que ChargeDTO et RevenuDTO sont bien là
import java.util.List;

public interface CaisseService {

    // --- FACTURES & SITUATION ---
    SituationFinanciereDTO initSituation(Long idDM);
    SituationFinanciereDTO getSituationByDossier(Long idDM);
    FactureDTO creerFacture(FactureDTO dto);
    FactureDTO getFactureById(Long id);
    List<FactureDTO> getFacturesByDossier(Long idSF);
    void payerFacture(Long idFacture, double montant);

    // --- GESTION FINANCIÈRE (Charges & Revenus) ---
    // C'est cette partie qui manquait dans ton interface !
    void ajouterCharge(ChargeDTO dto);
    List<ChargeDTO> getAllCharges();

    void ajouterRevenu(RevenuDTO dto);
    List<RevenuDTO> getAllRevenus();
}