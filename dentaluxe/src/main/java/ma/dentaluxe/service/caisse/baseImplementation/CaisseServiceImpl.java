package ma.dentaluxe.service.caisse.baseImplementation;

import ma.dentaluxe.entities.caisse.*;
import ma.dentaluxe.entities.enums.StatutFacture;
import ma.dentaluxe.entities.enums.StatutSituationFinanciere;
import ma.dentaluxe.mvc.dto.caisse.*;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.*;
import ma.dentaluxe.service.caisse.api.CaisseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CaisseServiceImpl implements CaisseService {

    private final FactureRepositoryImpl factureRepo = new FactureRepositoryImpl();
    private final SituationFinanciereRepositoryImpl sfRepo = new SituationFinanciereRepositoryImpl();
    private final ChargeRepositoryImpl chargeRepo = new ChargeRepositoryImpl();
    private final RevenuRepositoryImpl revenuRepo = new RevenuRepositoryImpl();

    @Override
    public SituationFinanciereDTO initSituation(Long idDM) {
        if (sfRepo.findByDossierMedicalId(idDM) != null) {
            return getSituationByDossier(idDM);
        }
        SituationFinanciere sf = new SituationFinanciere();
        sf.setIdDM(idDM);
        sf.setTotalDesActes(0.0); sf.setTotalPaye(0.0); sf.setResteDu(0.0);
        sf.setStatut(StatutSituationFinanciere.SOLDE);
        sfRepo.create(sf);
        return mapSFToDTO(sf);
    }

    @Override
    public SituationFinanciereDTO getSituationByDossier(Long idDM) {
        return mapSFToDTO(sfRepo.findByDossierMedicalId(idDM));
    }

    @Override
    public FactureDTO creerFacture(FactureDTO dto) {
        Facture f = new Facture();
        f.setIdSF(dto.getIdSF());
        f.setIdConsultation(dto.getIdConsultation());
        f.setTotalFacture(dto.getTotalFacture());
        f.setTotalDesActes(dto.getTotalFacture());
        f.setMontantPaye(0.0);
        f.setReste(dto.getTotalFacture());
        f.setStatut(StatutFacture.EN_ATTENTE);
        f.setDateCreation(LocalDateTime.now());

        factureRepo.create(f);

        // Update Situation
        SituationFinanciere sf = sfRepo.findById(dto.getIdSF());
        if(sf != null) {
            sf.setTotalDesActes(sf.getTotalDesActes() + f.getTotalFacture());
            sf.setResteDu(sf.getResteDu() + f.getTotalFacture());
            if(sf.getResteDu() > 0) sf.setStatut(StatutSituationFinanciere.DEBIT);
            sfRepo.update(sf);
        }
        return mapFactureToDTO(f);
    }

    @Override
    public void payerFacture(Long idFacture, double montant) {
        Facture f = factureRepo.findById(idFacture);
        if (f == null) return;

        f.setMontantPaye(f.getMontantPaye() + montant);
        f.setReste(f.getTotalFacture() - f.getMontantPaye());
        f.setStatut(f.getReste() <= 0.01 ? StatutFacture.PAYEE : StatutFacture.PARTIELLEMENT_PAYEE);
        factureRepo.update(f);
        sfRepo.updateAfterPayment(f.getIdSF(), montant);
    }

    @Override
    public FactureDTO getFactureById(Long id) {
        return mapFactureToDTO(factureRepo.findById(id));
    }

    @Override
    public List<FactureDTO> getFacturesByDossier(Long idSF) {
        return factureRepo.findBySituationFinanciereId(idSF).stream()
                .map(this::mapFactureToDTO).collect(Collectors.toList());
    }

    // --- Charges & Revenus ---
    @Override
    public void ajouterCharge(ChargeDTO dto) {
        Charge c = new Charge();
        c.setTitre(dto.getTitre());
        c.setDescription(dto.getDescription());
        c.setMontant(dto.getMontant());
        c.setDate(LocalDateTime.now());
        chargeRepo.create(c);
    }

    @Override
    public List<ChargeDTO> getAllCharges() {
        return chargeRepo.findAll().stream()
                .map(c -> new ChargeDTO(c.getIdCharge(), c.getTitre(), c.getDescription(), c.getMontant(), c.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public void ajouterRevenu(RevenuDTO dto) {
        Revenu r = new Revenu();
        r.setTitre(dto.getTitre());
        r.setDescription(dto.getDescription());
        r.setMontant(dto.getMontant());
        r.setDate(LocalDateTime.now());
        revenuRepo.create(r);
    }

    @Override
    public List<RevenuDTO> getAllRevenus() {
        return revenuRepo.findAll().stream()
                .map(r -> new RevenuDTO(r.getIdRevenu(), r.getTitre(), r.getDescription(), r.getMontant(), r.getDate()))
                .collect(Collectors.toList());
    }

    // Mappers
    private FactureDTO mapFactureToDTO(Facture f) {
        if(f==null) return null;
        return new FactureDTO(f.getIdFacture(), f.getIdSF(), f.getIdConsultation(), f.getTotalFacture(), f.getMontantPaye(),f.getTotalDesActes(), f.getReste(), f.getStatut(), f.getDateCreation());
    }

    private SituationFinanciereDTO mapSFToDTO(SituationFinanciere sf) {
        if(sf==null) return null;
        return new SituationFinanciereDTO(sf.getIdSF(), sf.getIdDM(), sf.getTotalDesActes(), sf.getTotalPaye(), sf.getResteDu(), sf.getStatut());
    }
}