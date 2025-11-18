package ma.dentaluxe.repository.modules.dashboard.inMemDB_implementation;

import ma.dentaluxe.repository.modules.dashboard.api.DashboardRepository;
import java.time.LocalDate;

public class DashboardRepositoryImpl implements DashboardRepository {
    @Override
    public int countPatientsTotal() {
        // TODO: compter tous les patients
        return 0;
    }

    @Override
    public int countPatientsToday() {
        // TODO: compter patients créés aujourd'hui
        return 0;
    }

    @Override
    public int countRDVToday() {
        // TODO: compter RDV d'aujourd'hui
        return 0;
    }

    @Override
    public int countRDVThisWeek() {
        // TODO: compter RDV de cette semaine
        return 0;
    }

    @Override
    public double calculateCAMensuel() {
        // TODO: calculer CA du mois
        return 0.0;
    }

    @Override
    public double calculateCAJour() {
        // TODO: calculer CA du jour
        return 0.0;
    }

    @Override
    public double calculateCATotal() {
        // TODO: calculer CA total
        return 0.0;
    }

}
