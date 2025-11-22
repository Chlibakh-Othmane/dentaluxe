package ma.dentaluxe.repository.modules.dashboard.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.repository.modules.dashboard.api.DashboardRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardRepositoryImpl implements DashboardRepository {

    private int getInt(String sql) {
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private double getDouble(String sql) {
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    private List<String> getListString(String sql, String col1, String col2) {
        List<String> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp(col2);
                list.add(rs.getString(col1) + ": " + (ts != null ? ts.toString() : "Never"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // ==============================
    // PATIENTS
    // ==============================
    @Override
    public int countPatientsTotal() {
        return getInt("SELECT COUNT(*) FROM Patient");
    }

    @Override
    public int countPatientsToday() {
        return getInt(
                "SELECT COUNT(*) FROM DossierMedical dm " +
                        "JOIN Patient p ON dm.idPatient = p.idPatient " +
                        "WHERE DATE(dm.dateDeCreation) = CURRENT_DATE"
        );
    }

    // ==============================
    // RDV
    // ==============================
    @Override
    public int countRDVToday() {
        return getInt("SELECT COUNT(*) FROM RDV WHERE DATE(dateRDV) = CURRENT_DATE");
    }

    @Override
    public int countRDVThisWeek() {
        return getInt("SELECT COUNT(*) FROM RDV WHERE YEARWEEK(dateRDV,1) = YEARWEEK(CURRENT_DATE,1)");
    }

    @Override
    public int countRDVThisMonth() {
        return getInt("SELECT COUNT(*) FROM RDV WHERE MONTH(dateRDV) = MONTH(CURRENT_DATE) AND YEAR(dateRDV) = YEAR(CURRENT_DATE)");
    }

    @Override
    public int countRDVThisYear() {
        return getInt("SELECT COUNT(*) FROM RDV WHERE YEAR(dateRDV) = YEAR(CURRENT_DATE)");
    }

    // ==============================
    // CONSULTATIONS
    // ==============================
    @Override
    public int countConsultationsToday() {
        return getInt("SELECT COUNT(*) FROM Consultation WHERE DATE(dateConsultation) = CURRENT_DATE");
    }

    @Override
    public int countConsultationsThisMonth() {
        return getInt("SELECT COUNT(*) FROM Consultation WHERE MONTH(dateConsultation) = MONTH(CURRENT_DATE) AND YEAR(dateConsultation) = YEAR(CURRENT_DATE)");
    }

    @Override
    public int countConsultationsThisYear() {
        return getInt("SELECT COUNT(*) FROM Consultation WHERE YEAR(dateConsultation) = YEAR(CURRENT_DATE)");
    }

    // ==============================
    // FINANCIALS / ACTES
    // ==============================
    @Override
    public double calculateCAMensuel() {
        return getDouble("SELECT SUM(prixDeBase) FROM Acte a " +
                "JOIN InterventionMedecin im ON a.idInterventionMedecin = im.idIM " +
                "WHERE MONTH(im.idConsultation) = MONTH(CURRENT_DATE) AND YEAR(im.idConsultation) = YEAR(CURRENT_DATE)");
    }

    @Override
    public double calculateCAJour() {
        return getDouble("SELECT SUM(prixDeBase) FROM Acte a " +
                "JOIN InterventionMedecin im ON a.idInterventionMedecin = im.idIM " +
                "JOIN Consultation c ON im.idConsultation = c.idConsultation " +
                "WHERE DATE(c.dateConsultation) = CURRENT_DATE");
    }

    @Override
    public double calculateCATotal() {
        return getDouble("SELECT SUM(prixDeBase) FROM Acte");
    }

    // ==============================
    // USERS / LAST LOGIN
    // ==============================
    @Override
    public List<String> getLastLoginDates() {
        return getListString("SELECT login, last_login_date FROM Utilisateur", "login", "last_login_date");
    }
}