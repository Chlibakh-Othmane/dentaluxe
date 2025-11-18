package ma.dentaluxe.repository.modules.dashboard.api;

import java.time.LocalDate;

public interface DashboardRepository {
    // Statistiques globales
    int countPatientsTotal();
    int countPatientsToday();
    int countRDVToday();
    int countRDVThisWeek();

    // Statistiques financières
    double calculateCAMensuel();
    double calculateCAJour();
    double calculateCATotal();

}
