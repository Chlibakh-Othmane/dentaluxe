package ma.dentaluxe.repository.modules.dossierMedical.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.consultation.Consultation;
import ma.dentaluxe.entities.enums.StatutConsultation;
import ma.dentaluxe.repository.modules.dossierMedical.api.ConsultationRepository;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsultationRepositoryImpl implements ConsultationRepository {

    @Override
    public List<Consultation> findAll() {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation ORDER BY dateConsultation DESC";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public Consultation findById(Long id) {
        String sql = "SELECT * FROM Consultation WHERE idConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToConsultation(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(Consultation consultation) {
        String sql = "INSERT INTO Consultation (idRDV, idDM, idMedecin, dateConsultation, statut, observation, motif) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setObject(1, consultation.getIdRDV(), Types.BIGINT);
            pstmt.setLong(2, consultation.getIdDM());
            pstmt.setLong(3, consultation.getIdMedecin());
            pstmt.setDate(4, Date.valueOf(consultation.getDateConsultation()));
            pstmt.setString(5, consultation.getStatut().name());
            pstmt.setString(6, consultation.getObservation());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                consultation.setIdConsultation(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Consultation consultation) {
        String sql = "UPDATE Consultation SET idRDV = ?, idDM = ?, idMedecin = ?, dateConsultation = ?, " +
                "statut = ?, observation = ?,  WHERE idConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, consultation.getIdRDV(), Types.BIGINT);
            pstmt.setLong(2, consultation.getIdDM());
            pstmt.setLong(3, consultation.getIdMedecin());
            pstmt.setDate(4, Date.valueOf(consultation.getDateConsultation()));
            pstmt.setString(5, consultation.getStatut().name());
            pstmt.setString(6, consultation.getObservation());
            pstmt.setLong(7, consultation.getIdConsultation());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Consultation consultation) {
        deleteById(consultation.getIdConsultation());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM Consultation WHERE idConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Consultation> findByDossierMedicalId(Long idDM) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE idDM = ? ORDER BY dateConsultation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idDM);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByMedecinId(Long idMedecin) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE idMedecin = ? ORDER BY dateConsultation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idMedecin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByDate(LocalDate date) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE dateConsultation = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByRDVId(Long idRDV) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE idRDV = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idRDV);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    @Override
    public List<Consultation> findByStatut(StatutConsultation statut) {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT * FROM Consultation WHERE statut = ? ORDER BY dateConsultation DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statut.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                consultations.add(mapResultSetToConsultation(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return consultations;
    }

    private Consultation mapResultSetToConsultation(ResultSet rs) throws SQLException {
        return Consultation.builder()
                .idConsultation(rs.getLong("idConsultation"))
                .idRDV((Long) rs.getObject("idRDV"))
                .idDM(rs.getLong("idDM"))
                .idMedecin(rs.getLong("idMedecin"))
                .dateConsultation(rs.getDate("dateConsultation").toLocalDate())
                .statut(StatutConsultation.valueOf(rs.getString("statut")))
                .observation(rs.getString("observation"))
                .build();
    }
}