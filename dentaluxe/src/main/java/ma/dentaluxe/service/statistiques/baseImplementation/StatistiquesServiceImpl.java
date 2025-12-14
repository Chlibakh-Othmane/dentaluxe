package ma.dentaluxe.service.statistiques.baseImplementation;

import ma.dentaluxe.entities.caisse.*;
import ma.dentaluxe.entities.patient.Patient;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.mvc.dto.statistiques.BilanFinancierDTO;
import ma.dentaluxe.repository.modules.agenda.inMemDB_implementation.RDVRepositoryImpl;
import ma.dentaluxe.repository.modules.caisse.inMemDB_implementation.*;
import ma.dentaluxe.repository.modules.patient.inMemDB_implementation.PatientRepositoryImpl;
import ma.dentaluxe.service.statistiques.api.StatistiquesService;

import java.time.LocalDate;
import java.util.List;

public class StatistiquesServiceImpl implements StatistiquesService {

    private final FactureRepositoryImpl factureRepo = new FactureRepositoryImpl();
    private final ChargeRepositoryImpl chargeRepo = new ChargeRepositoryImpl();
    private final RevenuRepositoryImpl revenuRepo = new RevenuRepositoryImpl();
    private final RDVRepositoryImpl rdvRepo = new RDVRepositoryImpl();
    private final PatientRepositoryImpl patientRepo = new PatientRepositoryImpl();

    @Override
    public BilanFinancierDTO getBilanJournalier(LocalDate date) {
        return calculerBilan(date, "JOUR");
    }

    @Override
    public BilanFinancierDTO getBilanMensuel(int mois, int annee) {
        return calculerBilan(LocalDate.of(annee, mois, 1), "MOIS");
    }

    @Override
    public BilanFinancierDTO getBilanAnnuel(int annee) {
        return calculerBilan(LocalDate.of(annee, 1, 1), "ANNEE");
    }

    private BilanFinancierDTO calculerBilan(LocalDate dateRef, String type) {
        List<Facture> factures = factureRepo.findAll();
        List<Charge> charges = chargeRepo.findAll();
        List<Revenu> revenus = revenuRepo.findAll();
        List<RDV> rdvs = rdvRepo.findAll();
        List<Patient> patients = patientRepo.findAll();

        double recettes = factures.stream()
                .filter(f -> isSamePeriod(f.getDateCreation().toLocalDate(), dateRef, type))
                .mapToDouble(f -> f.getMontantPaye() == null ? 0.0 : f.getMontantPaye())
                .sum();

        double depenses = charges.stream()
                .filter(c -> isSamePeriod(c.getDate().toLocalDate(), dateRef, type))
                .mapToDouble(c -> c.getMontant() == null ? 0.0 : c.getMontant())
                .sum();

        double autresRevenus = revenus.stream()
                .filter(r -> isSamePeriod(r.getDate().toLocalDate(), dateRef, type))
                .mapToDouble(r -> r.getMontant() == null ? 0.0 : r.getMontant())
                .sum();

        long nbRdv = rdvs.stream()
                .filter(r -> isSamePeriod(r.getDateRDV(), dateRef, type))
                .count();

        long nbNouveaux = patients.stream()
                .filter(p -> p.getDateCreation() != null && isSamePeriod(p.getDateCreation().toLocalDate(), dateRef, type))
                .count();

        return new BilanFinancierDTO(
                type + " " + dateRef.toString(),
                recettes,
                autresRevenus,
                depenses,
                (recettes + autresRevenus) - depenses,
                nbRdv,
                nbNouveaux
        );
    }

    private boolean isSamePeriod(LocalDate d1, LocalDate ref, String type) {
        if (d1 == null) return false;
        if (type.equals("JOUR")) return d1.equals(ref);
        if (type.equals("MOIS")) return d1.getMonth() == ref.getMonth() && d1.getYear() == ref.getYear();
        if (type.equals("ANNEE")) return d1.getYear() == ref.getYear();
        return false;
    }
}