package ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.ordonnance.Prescription;
import ma.dentaluxe.repository.modules.ordonnance.api.PrescriptionRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionRepositoryImpl implements PrescriptionRepository {

    @Override
    public List<Prescription> findAll() {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescription ORDER BY idPrescription";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                prescriptions.add(mapResultSetToPrescription(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }

    @Override
    public Prescription findById(Long id) {
        String sql = "SELECT * FROM prescription WHERE idPrescription = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToPrescription(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Prescription prescription) {
        String sql = "INSERT INTO prescription (idOrdo, idMedicament, quantite, frequence, dureeEnJours) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, prescription.getIdOrdo());
            pstmt.setLong(2, prescription.getIdMedicament());
            pstmt.setInt(3, prescription.getQuantite());
            pstmt.setString(4, prescription.getFrequence());
            pstmt.setInt(5, prescription.getDureeEnJours());

            pstmt.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                prescription.setIdPrescription(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Prescription prescription) {
        String sql = "UPDATE prescription SET idOrdo = ?, idMedicament = ?, quantite = ?, frequence = ?, dureeEnJours = ? WHERE idPrescription = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, prescription.getIdOrdo());
            pstmt.setLong(2, prescription.getIdMedicament());
            pstmt.setInt(3, prescription.getQuantite());
            pstmt.setString(4, prescription.getFrequence());
            pstmt.setInt(5, prescription.getDureeEnJours());
            pstmt.setLong(6, prescription.getIdPrescription());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Prescription prescription) {
        deleteById(prescription.getIdPrescription());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM prescription WHERE idPrescription = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Prescription> findByOrdonnance(Long idOrdo) {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescription WHERE idOrdo = ? ORDER BY idPrescription";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idOrdo);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                prescriptions.add(mapResultSetToPrescription(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prescriptions;
    }

    @Override
    public void deleteByOrdonnance(Long idOrdo) {
        String sql = "DELETE FROM prescription WHERE idOrdo = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idOrdo);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Prescription mapResultSetToPrescription(ResultSet rs) throws SQLException {
        return Prescription.builder()
                .idPrescription(rs.getLong("idPrescription"))
                .idOrdo(rs.getLong("idOrdo"))
                .idMedicament(rs.getLong("idMedicament"))
                .quantite(rs.getInt("quantite"))
                .frequence(rs.getString("frequence"))
                .dureeEnJours(rs.getInt("dureeEnJours"))
                .build();
    }
}