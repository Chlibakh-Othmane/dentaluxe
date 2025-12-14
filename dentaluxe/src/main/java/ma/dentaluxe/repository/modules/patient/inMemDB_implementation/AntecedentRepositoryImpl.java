package ma.dentaluxe.repository.modules.patient.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.patient.Antecedent;
import ma.dentaluxe.entities.enums.CategorieAntecedent;
import ma.dentaluxe.entities.enums.NiveauRisque;
import ma.dentaluxe.repository.modules.patient.api.AntecedentRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AntecedentRepositoryImpl implements AntecedentRepository {

    // ==================== CRUD DE BASE ====================

    @Override
    public List<Antecedent> findAll() {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM antecedent ORDER BY nom";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    @Override
    public Object findById(Object o) {
        return null;
    }

    @Override
    public void create(Object patient) {

    }

    @Override
    public void update(Object patient) {

    }

    @Override
    public void delete(Object patient) {

    }

    @Override
    public void deleteById(Object o) {

    }

    @Override
    public Antecedent findById(Long id) {
        String sql = "SELECT * FROM antecedent WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToAntecedent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Antecedent antecedent) {
        String sql = "INSERT INTO antecedent (nom, categorie, niveau_risque, date_creation) VALUES (?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, antecedent.getNom());
            pstmt.setString(2, antecedent.getCategorie().name());
            pstmt.setString(3, antecedent.getNiveauRisque().name());
            pstmt.setDate(4, Date.valueOf(LocalDate.now()));

            pstmt.executeUpdate();

            // Récupérer l'ID généré
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                antecedent.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Antecedent antecedent) {
        String sql = "UPDATE antecedent SET nom = ?, categorie = ?, niveau_risque = ? WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, antecedent.getNom());
            pstmt.setString(2, antecedent.getCategorie().name());
            pstmt.setString(3, antecedent.getNiveauRisque().name());
            pstmt.setLong(4, antecedent.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Antecedent antecedent) {
        deleteById(antecedent.getId());
    }

    @Override
    public void deleteById(Long id) {
        // D'abord supprimer les associations
        String sqlDeleteAssociations = "DELETE FROM patient_antecedent WHERE antecedent_id = ?";
        String sqlDeleteAntecedent = "DELETE FROM antecedent WHERE id = ?";

        try (Connection conn = Db.getConnection()) {
            // Supprimer les associations
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlDeleteAssociations)) {
                pstmt1.setLong(1, id);
                pstmt1.executeUpdate();
            }

            // Supprimer l'antécédent
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlDeleteAntecedent)) {
                pstmt2.setLong(1, id);
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== MÉTHODES SPÉCIFIQUES ====================

    @Override
    public List<Antecedent> findByNom(String nom) {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM antecedent WHERE nom LIKE ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nom + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    @Override
    public List<Antecedent> findByCategorie(CategorieAntecedent categorie) {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM antecedent WHERE categorie = ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categorie.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    @Override
    public List<Antecedent> findByNiveauRisque(NiveauRisque niveauRisque) {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = "SELECT * FROM antecedent WHERE niveau_risque = ? ORDER BY nom";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, niveauRisque.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    @Override
    public List<Antecedent> findByPatientId(Long patientId) {
        List<Antecedent> antecedents = new ArrayList<>();
        String sql = """
            SELECT a.* FROM antecedent a
            INNER JOIN patient_antecedent pa ON a.id = pa.antecedent_id
            WHERE pa.patient_id = ?
            ORDER BY a.nom
            """;

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, patientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                antecedents.add(mapResultSetToAntecedent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return antecedents;
    }

    @Override
    public void addAntecedentToPatient(Long patientId, Long antecedentId, String notes) {
        String sql = "INSERT INTO patient_antecedent (patient_id, antecedent_id, notes, date_association) VALUES (?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, patientId);
            pstmt.setLong(2, antecedentId);
            pstmt.setString(3, notes != null ? notes : "");
            pstmt.setDate(4, Date.valueOf(LocalDate.now()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAntecedentFromPatient(Long patientId, Long antecedentId) {
        String sql = "DELETE FROM patient_antecedent WHERE patient_id = ? AND antecedent_id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, patientId);
            pstmt.setLong(2, antecedentId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getNotesForPatient(Long patientId, Long antecedentId) {
        String sql = "SELECT notes FROM patient_antecedent WHERE patient_id = ? AND antecedent_id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, patientId);
            pstmt.setLong(2, antecedentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("notes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void updateNotesForPatient(Long patientId, Long antecedentId, String notes) {
        String sql = "UPDATE patient_antecedent SET notes = ? WHERE patient_id = ? AND antecedent_id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, notes);
            pstmt.setLong(2, patientId);
            pstmt.setLong(3, antecedentId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== MÉTHODE UTILITAIRE ====================

    private Antecedent mapResultSetToAntecedent(ResultSet rs) throws SQLException {
        return Antecedent.builder()
                .id(rs.getLong("id"))
                .nom(rs.getString("nom"))
                .categorie(CategorieAntecedent.valueOf(rs.getString("categorie")))
                .niveauRisque(NiveauRisque.valueOf(rs.getString("niveau_risque")))
                .dateCreation(rs.getDate("date_creation") != null ? rs.getDate("date_creation").toLocalDate() : null)
                .build();
    }
}