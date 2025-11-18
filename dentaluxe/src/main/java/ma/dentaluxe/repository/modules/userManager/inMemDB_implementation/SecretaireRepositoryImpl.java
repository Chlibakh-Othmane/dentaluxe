package ma.dentaluxe.repository.modules.userManager.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.utilisateur.Secretaire;
import ma.dentaluxe.entities.utilisateur.Staff;
import ma.dentaluxe.repository.modules.userManager.api.SecretaireRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SecretaireRepositoryImpl extends UtilisateurRepositoryImpl implements SecretaireRepository {

    @Override
    public Secretaire findByNumCNSS(String numCNSS) {
        String sql = "SELECT u.id as user_id, u.nom, u.prenom, u.email, u.tel, u.login, u.actif, " +
                "st.id as staff_id, st.salaire, st.prime, st.date_recrutement, st.solde_conge, " +
                "s.num_cnss, s.commission " +
                "FROM Secretaire s " +
                "JOIN Staff st ON s.id=st.id " +
                "JOIN Utilisateur u ON st.id=u.id " +
                "WHERE s.num_cnss=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, numCNSS);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return mapSecretaire(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Secretaire> findAllByCommission(double min, double max) {
        List<Secretaire> list = new ArrayList<>();
        String sql = "SELECT u.id as user_id, u.nom, u.prenom, u.email, u.tel, u.login, u.actif, " +
                "st.id as staff_id, st.salaire, st.prime, st.date_recrutement, st.solde_conge, " +
                "s.num_cnss, s.commission " +
                "FROM Secretaire s " +
                "JOIN Staff st ON s.id=st.id " +
                "JOIN Utilisateur u ON st.id=u.id " +
                "WHERE s.commission BETWEEN ? AND ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, min);
            pstmt.setDouble(2, max);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(mapSecretaire(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Secretaire mapSecretaire(ResultSet rs) throws SQLException {
        // Création du staff
        Staff staff = new Staff();
        staff.setId(rs.getLong("staff_id"));
        staff.setSalaire(rs.getBigDecimal("salaire"));
        staff.setPrime(rs.getBigDecimal("prime"));
        staff.setDateRecrutement(rs.getDate("date_recrutement") != null ? rs.getDate("date_recrutement").toLocalDate() : null);
        staff.setSoldeConge(rs.getInt("solde_conge"));

        // Création du secrétaire
        Secretaire s = new Secretaire();
        s.setId(rs.getLong("user_id"));
        s.setNumCnss(rs.getString("num_cnss"));
        s.setCommission(rs.getBigDecimal("commission"));
        s.setStaff(staff);

        return s;
    }
}