package ma.dentaluxe.repository.modules.userManager.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.utilisateur.Medecin;
import ma.dentaluxe.entities.utilisateur.Staff;
import ma.dentaluxe.repository.modules.userManager.api.MedecinRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedecinRepositoryImpl extends SecretaireRepositoryImpl implements MedecinRepository {

    @Override
    public List<Medecin> findBySpecialite(String specialite) {
        List<Medecin> list = new ArrayList<>();
        String sql = "SELECT u.id as user_id, u.nom, u.prenom, u.email, u.tel, u.login, u.actif, " +
                "st.id as staff_id, st.salaire, st.prime, st.date_recrutement, st.solde_conge, " +
                "m.specialite, m.agenda_medecin " +
                "FROM Medecin m " +
                "JOIN Staff st ON m.id=st.id " +
                "JOIN Utilisateur u ON st.id=u.id " +
                "WHERE m.specialite=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, specialite);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) list.add(mapMedecin(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Medecin mapMedecin(ResultSet rs) throws SQLException {
        // Création du staff
        Staff staff = new Staff();
        staff.setId(rs.getLong("staff_id"));
        staff.setSalaire(rs.getBigDecimal("salaire"));
        staff.setPrime(rs.getBigDecimal("prime"));
        staff.setDateRecrutement(rs.getDate("date_recrutement") != null ? rs.getDate("date_recrutement").toLocalDate() : null);
        staff.setSoldeConge(rs.getInt("solde_conge"));

        // Création du médecin
        Medecin m = new Medecin();
        m.setId(rs.getLong("user_id"));
        m.setSpecialite(rs.getString("specialite"));
        m.setAgendaMedecin(rs.getString("agenda_medecin"));
        m.setStaff(staff);

        return m;
    }
}