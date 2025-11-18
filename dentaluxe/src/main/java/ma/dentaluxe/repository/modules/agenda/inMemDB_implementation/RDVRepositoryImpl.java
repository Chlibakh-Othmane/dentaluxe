package ma.dentaluxe.repository.modules.agenda.inMemDB_implementation;
import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.enums.StatutRDV;
import ma.dentaluxe.entities.rdv.RDV;
import ma.dentaluxe.repository.modules.agenda.api.RDVRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class RDVRepositoryImpl implements RDVRepository {

    @Override
    public List<RDV> findAll() {
        List<RDV> rdvs = new ArrayList<>();
        String sql = "SELECT * FROM rdv ORDER BY dateRDV, heureRDV";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                rdvs.add(mapResultSetToRDV(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rdvs;
    }

    @Override
    public RDV findById(Long id) {
        String sql = "SELECT * FROM rdv WHERE idRDV = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToRDV(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void create(RDV rdv) {
        String sql = "INSERT INTO rdv (idDM, idMedecin, dateRDV, heureRDV, motif, statut, noteMedecin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, rdv.getIdDM());
            pstmt.setLong(2, rdv.getIdMedecin());
            pstmt.setDate(3, Date.valueOf(rdv.getDateRDV()));
            pstmt.setTime(4, Time.valueOf(rdv.getHeureRDV()));
            pstmt.setString(5, rdv.getMotif());
            pstmt.setString(6, rdv.getStatut().name());
            pstmt.setString(7, rdv.getNoteMedecin());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                rdv.setIdRDV(rs.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(RDV rdv) {
        String sql = "UPDATE rdv SET idDM = ?, idMedecin = ?, dateRDV = ?, heureRDV = ?, " +
                "motif = ?, statut = ?, noteMedecin = ? WHERE idRDV = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, rdv.getIdDM());
            pstmt.setLong(2, rdv.getIdMedecin());
            pstmt.setDate(3, Date.valueOf(rdv.getDateRDV()));
            pstmt.setTime(4, Time.valueOf(rdv.getHeureRDV()));
            pstmt.setString(5, rdv.getMotif());
            pstmt.setString(6, rdv.getStatut().name());
            pstmt.setString(7, rdv.getNoteMedecin());
            pstmt.setLong(8, rdv.getIdRDV());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(RDV rdv) {
        deleteById(rdv.getIdRDV());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM rdv WHERE idRDV = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<RDV> findByDate(LocalDate date) {
        List<RDV> rdvs = new ArrayList<>();
        String sql = "SELECT * FROM rdv WHERE dateRDV = ? ORDER BY heureRDV";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(date));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rdvs.add(mapResultSetToRDV(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rdvs;
    }

    @Override
    public List<RDV> findByMedecinId(Long idMedecin) {
        List<RDV> rdvs = new ArrayList<>();
        String sql = "SELECT * FROM rdv WHERE idMedecin = ? ORDER BY dateRDV, heureRDV";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idMedecin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rdvs.add(mapResultSetToRDV(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rdvs;
    }

    @Override
    public List<RDV> findByPatientDossierId(Long idDM) {
        List<RDV> rdvs = new ArrayList<>();
        String sql = "SELECT * FROM rdv WHERE idDM = ? ORDER BY dateRDV DESC, heureRDV DESC";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, idDM);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rdvs.add(mapResultSetToRDV(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rdvs;
    }

    @Override
    public List<RDV> findByStatut(StatutRDV statut) {
        List<RDV> rdvs = new ArrayList<>();
        String sql = "SELECT * FROM rdv WHERE statut = ? ORDER BY dateRDV, heureRDV";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statut.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rdvs.add(mapResultSetToRDV(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rdvs;
    }

    @Override
    public List<RDV> findByDateRange(LocalDate debut, LocalDate fin) {
        List<RDV> rdvs = new ArrayList<>();
        String sql = "SELECT * FROM rdv WHERE dateRDV BETWEEN ? AND ? ORDER BY dateRDV, heureRDV";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(debut));
            pstmt.setDate(2, Date.valueOf(fin));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rdvs.add(mapResultSetToRDV(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rdvs;
    }

    private RDV mapResultSetToRDV(ResultSet rs) throws SQLException {
        return RDV.builder()
                .idRDV(rs.getLong("idRDV"))
                .idDM(rs.getLong("idDM"))
                .idMedecin(rs.getLong("idMedecin"))
                .dateRDV(rs.getDate("dateRDV").toLocalDate())
                .heureRDV(rs.getTime("heureRDV").toLocalTime())
                .motif(rs.getString("motif"))
                .statut(StatutRDV.valueOf(rs.getString("statut")))
                .noteMedecin(rs.getString("noteMedecin"))
                .build();
    }
}



