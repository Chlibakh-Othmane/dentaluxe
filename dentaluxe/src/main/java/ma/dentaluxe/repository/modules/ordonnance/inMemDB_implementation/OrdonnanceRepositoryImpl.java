package ma.dentaluxe.repository.modules.ordonnance.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.ordonnance.Ordonnance;
import ma.dentaluxe.repository.modules.ordonnance.api.OrdonnanceRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdonnanceRepositoryImpl implements OrdonnanceRepository {

    @Override
    public List<Ordonnance> findAll() {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String sql = "SELECT * FROM ordonnance ORDER BY dateOrdonnance DESC";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ordonnances.add(mapResultSetToOrdonnance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordonnances;
    }

    @Override
    public Ordonnance findById(Long id) {
        String sql = "SELECT * FROM ordonnance WHERE idOrdo = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToOrdonnance(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Ordonnance ordonnance) {
        String sql = "INSERT INTO ordonnance (idDM, idMedecin, dateOrdonnance) VALUES (?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, ordonnance.getIdDM());
            pstmt.setLong(2, ordonnance.getIdMedecin());
            pstmt.setDate(3, Date.valueOf(ordonnance.getDateOrdonnance()));

            pstmt.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                ordonnance.setIdOrdo(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Ordonnance ordonnance) {
        String sql = "UPDATE ordonnance SET idDM = ?, idMedecin = ?, dateOrdonnance = ? WHERE idOrdo = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, ordonnance.getIdDM());
            pstmt.setLong(2, ordonnance.getIdMedecin());
            pstmt.setDate(3, Date.valueOf(ordonnance.getDateOrdonnance()));
            pstmt.setLong(4, ordonnance.getIdOrdo());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Ordonnance ordonnance) {
        deleteById(ordonnance.getIdOrdo());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM ordonnance WHERE idOrdo = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ordonnance> findByDossierMedical(Long idDM) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String sql = "SELECT * FROM ordonnance WHERE idDM = ? ORDER BY dateOrdonnance DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idDM);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ordonnances.add(mapResultSetToOrdonnance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordonnances;
    }

    @Override
    public List<Ordonnance> findByMedecin(Long idMedecin) {
        List<Ordonnance> ordonnances = new ArrayList<>();
        String sql = "SELECT * FROM ordonnance WHERE idMedecin = ? ORDER BY dateOrdonnance DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idMedecin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                ordonnances.add(mapResultSetToOrdonnance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ordonnances;
    }

    private Ordonnance mapResultSetToOrdonnance(ResultSet rs) throws SQLException {
        return Ordonnance.builder()
                .idOrdo(rs.getLong("idOrdo"))
                .idDM(rs.getLong("idDM"))
                .idMedecin(rs.getLong("idMedecin"))
                .dateOrdonnance(rs.getDate("dateOrdonnance").toLocalDate())
                .build();
    }
}