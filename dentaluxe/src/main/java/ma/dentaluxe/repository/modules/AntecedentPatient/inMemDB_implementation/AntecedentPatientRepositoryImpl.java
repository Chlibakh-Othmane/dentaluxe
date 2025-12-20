package ma.dentaluxe.repository.modules.AntecedentPatient.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.AntecedentPatient.AntecedentPatient;
import ma.dentaluxe.repository.modules.AntecedentPatient.api.AntecedentPatientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AntecedentPatientRepositoryImpl implements AntecedentPatientRepository {

    // =========================
    // FIND ALL
    // =========================
    @Override
    public List<AntecedentPatient> findAll() {
        String sql = "SELECT * FROM antecedent_patient";
        List<AntecedentPatient> list = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // =========================
    // FIND BY ID (idAntecedent)
    // =========================
    @Override
    public AntecedentPatient findById(Long idAntecedent) {
        String sql = """
            SELECT * FROM antecedent_patient
            WHERE idAntecedent = ?
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idAntecedent);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return map(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // =========================
    // CREATE
    // =========================
    @Override
    public void create(AntecedentPatient ap) {
        String sql = """
            INSERT INTO antecedent_patient
            (idAntecedent, idPatient, dateAjout, actif, notes)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, ap.getIdAntecedent());
            ps.setLong(2, ap.getIdPatient());
            ps.setDate(3, Date.valueOf(ap.getDateAjout()));
            ps.setBoolean(4, ap.isActif());
            ps.setString(5, ap.getNotes());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur CREATE AntecedentPatient", e);
        }
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    public void update(AntecedentPatient ap) {
        String sql = """
            UPDATE antecedent_patient
            SET dateAjout = ?, actif = ?, notes = ?
            WHERE idAntecedent = ? AND idPatient = ?
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(ap.getDateAjout()));
            ps.setBoolean(2, ap.isActif());
            ps.setString(3, ap.getNotes());
            ps.setLong(4, ap.getIdAntecedent());
            ps.setLong(5, ap.getIdPatient());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur UPDATE AntecedentPatient", e);
        }
    }

    // =========================
    // DELETE (ENTITY)
    // =========================
    @Override
    public void delete(AntecedentPatient ap) {
        deleteById(ap.getIdAntecedent());
    }

    // =========================
    // DELETE BY ID (idAntecedent)
    // =========================
    @Override
    public void deleteById(Long idAntecedent) {
        String sql = """
            DELETE FROM antecedent_patient
            WHERE idAntecedent = ?
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idAntecedent);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur DELETE AntecedentPatient", e);
        }
    }

    // =========================
    // FIND BY PATIENT
    // =========================
    @Override
    public List<AntecedentPatient> findByPatientId(Long idPatient) {
        String sql = """
            SELECT * FROM antecedent_patient
            WHERE idPatient = ?
        """;

        List<AntecedentPatient> list = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idPatient);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // =========================
    // COUNT
    // =========================
    @Override
    public int countByPatientId(Long idPatient) {
        String sql = """
            SELECT COUNT(*)
            FROM antecedent_patient
            WHERE idPatient = ?
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idPatient);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    // =========================
    // DELETE BY PATIENT
    // =========================
    @Override
    public void deleteByPatientId(Long idPatient) {
        String sql = """
            DELETE FROM antecedent_patient
            WHERE idPatient = ?
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idPatient);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // =========================
    // MAPPING
    // =========================
    private AntecedentPatient map(ResultSet rs) throws SQLException {
        return AntecedentPatient.builder()
                .idAntecedent(rs.getLong("idAntecedent"))
                .idPatient(rs.getLong("idPatient"))
                .dateAjout(rs.getDate("dateAjout").toLocalDate())
                .actif(rs.getBoolean("actif"))
                .notes(rs.getString("notes"))
                .build();
    }
}
