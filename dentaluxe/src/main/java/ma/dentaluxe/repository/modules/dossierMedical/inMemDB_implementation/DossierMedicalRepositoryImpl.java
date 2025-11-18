package ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.dossier.DossierMedical;
import ma.dentaluxe.repository.modules.dossierMedical.api.DossierMedicalRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DossierMedicalRepositoryImpl implements DossierMedicalRepository {

    @Override
    public List<DossierMedical> findAll() {
        List<DossierMedical> dossiers = new ArrayList<>();
        String sql = "SELECT * FROM DossierMedical";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dossiers.add(mapResultSetToDossierMedical(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dossiers;
    }

    @Override
    public DossierMedical findById(Long id) {
        String sql = "SELECT * FROM DossierMedical WHERE idDM = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToDossierMedical(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(DossierMedical dossier) {
        String sql = "INSERT INTO DossierMedical (idPatient, dateDeCreation) VALUES (?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, dossier.getIdPatient());
            pstmt.setDate(2, Date.valueOf(dossier.getDateDeCreation()));

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                dossier.setIdDM(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DossierMedical dossier) {
        String sql = "UPDATE DossierMedical SET idPatient = ?, dateDeCreation = ? WHERE idDM = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, dossier.getIdPatient());
            pstmt.setDate(2, Date.valueOf(dossier.getDateDeCreation()));
            pstmt.setLong(3, dossier.getIdDM());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(DossierMedical dossier) {
        deleteById(dossier.getIdDM());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM DossierMedical WHERE idDM = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DossierMedical findByPatientId(Long idPatient) {
        String sql = "SELECT * FROM DossierMedical WHERE idPatient = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idPatient);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToDossierMedical(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DossierMedical mapResultSetToDossierMedical(ResultSet rs) throws SQLException {
        return DossierMedical.builder()
                .idDM(rs.getLong("idDM"))
                .idPatient(rs.getLong("idPatient"))
                .dateDeCreation(rs.getDate("dateDeCreation").toLocalDate())
                .build();
    }
}