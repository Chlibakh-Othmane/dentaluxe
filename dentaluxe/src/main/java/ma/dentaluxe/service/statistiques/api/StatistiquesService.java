package ma.dentaluxe.service.statistiques.api;

import ma.dentaluxe.mvc.dto.statistiques.BilanFinancierDTO;
import java.time.LocalDate;

public interface StatistiquesService {
    BilanFinancierDTO getBilanJournalier(LocalDate date);
    BilanFinancierDTO getBilanMensuel(int mois, int annee);
    BilanFinancierDTO getBilanAnnuel(int annee);
}