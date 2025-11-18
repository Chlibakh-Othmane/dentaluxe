package ma.dentaluxe.repository.modules.dashboard.api;

import java.util.List;

public interface DashboardRepository {

    // Statistiques globales / Patients

    int countPatientsTotal();
    int countPatientsToday();

    // Statistiques RDV

    int countRDVToday();
    int countRDVThisWeek();
    int countRDVThisMonth();
    int countRDVThisYear();

    // Statistiques Consultations

    int countConsultationsToday();
    int countConsultationsThisMonth();
    int countConsultationsThisYear();

    // Statistiques financières / Actes

    double calculateCAMensuel();
    double calculateCAJour();
    double calculateCATotal();

    // Users / last login

    List<String> getLastLoginDates(); // format: "login: lastLoginDate"
}