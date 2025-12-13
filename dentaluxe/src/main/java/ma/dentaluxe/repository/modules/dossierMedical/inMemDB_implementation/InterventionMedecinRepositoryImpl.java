package ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.consultation.InterventionMedecin;
import ma.dentaluxe.repository.modules.dossierMedical.api.InterventionMedecinRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterventionMedecinRepositoryImpl implements InterventionMedecinRepository {

    @Override
    public List<InterventionMedecin> findAll() {
        List<InterventionMedecin> interventions = new ArrayList<>();
        String sql = "SELECT * FROM interventionMedecin";
        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                interventions.add(mapResultSetToInterventionMedecin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interventions;
    }

    @Override
    public InterventionMedecin findById(Long id) {
        String sql = "SELECT * FROM interventionMedecin WHERE idIM = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToInterventionMedecin(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(InterventionMedecin interventionMedecin) {
        String sql = "INSERT INTO interventionMedecin (idMedecin, idConsultation, idActe, numDent, prixIntervention) VALUES (?, ?, ?, ? , ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, interventionMedecin.getIdMedecin());
            pstmt.setLong(2, interventionMedecin.getIdConsultation());
            pstmt.setLong(3, interventionMedecin.getIdActe());
            pstmt.setInt(4, interventionMedecin.getNumDent());
            pstmt.setDouble(5, interventionMedecin.getPrixIntervention());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                interventionMedecin.setIdIM(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(InterventionMedecin interventionMedecin) {
        String sql = "UPDATE interventionMedecin SET idMedecin = ?, idConsultation = ?, numDent = ?, prixIntervention = ? WHERE idIM = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, interventionMedecin.getIdMedecin());
            pstmt.setLong(2, interventionMedecin.getIdConsultation());
            pstmt.setInt(3, interventionMedecin.getNumDent());
            pstmt.setDouble(4, interventionMedecin.getPrixIntervention());
            pstmt.setLong(5, interventionMedecin.getIdIM());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(InterventionMedecin interventionMedecin) {
        deleteById(interventionMedecin.getIdIM());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM interventionMedecin WHERE idIM = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<InterventionMedecin> findByIdMedecin(Long idMedecin) {
        List<InterventionMedecin> interventions = new ArrayList<>();
        String sql = "SELECT * FROM interventionMedecin WHERE idMedecin = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idMedecin);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                interventions.add(mapResultSetToInterventionMedecin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interventions;
    }

    @Override
    public List<InterventionMedecin> findByIdConsultation(Long idConsultation) {
        List<InterventionMedecin> interventions = new ArrayList<>();
        String sql = "SELECT * FROM interventionMedecin WHERE idConsultation = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, idConsultation);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                interventions.add(mapResultSetToInterventionMedecin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interventions;
    }

    @Override
    public List<InterventionMedecin> findByNumDent(Integer numDent) {
        List<InterventionMedecin> interventions = new ArrayList<>();
        String sql = "SELECT * FROM interventionMedecin WHERE numDent = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, numDent);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                interventions.add(mapResultSetToInterventionMedecin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interventions;
    }

    @Override
    public List<InterventionMedecin> findByPrixInterventionBetween(Double minPrix, Double maxPrix) {
        List<InterventionMedecin> interventions = new ArrayList<>();
        String sql = "SELECT * FROM interventionMedecin WHERE prixIntervention BETWEEN ? AND ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, minPrix);
            pstmt.setDouble(2, maxPrix);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                interventions.add(mapResultSetToInterventionMedecin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return interventions;
    }

    private InterventionMedecin mapResultSetToInterventionMedecin(ResultSet rs) throws SQLException {
        return InterventionMedecin.builder()
                .idIM(rs.getLong("idIM"))
                .idMedecin(rs.getLong("idMedecin"))
                .idConsultation(rs.getLong("idConsultation"))
                .numDent(rs.getInt("numDent"))
                .prixIntervention(rs.getDouble("prixIntervention"))
                .build();
    }
}
