package ma.dentaluxe.repository.modules.auth.inMemDB_implementation;

import ma.dentaluxe.conf.Db;
import ma.dentaluxe.entities.utilisateur.Utilisateur;
import ma.dentaluxe.repository.modules.auth.api.AuthRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuthRepositoryImpl implements AuthRepository {

    // ============================
    //   FIND ALL
    // ============================
    @Override
    public List<Utilisateur> findAll() {
        List<Utilisateur> users = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Connection conn = Db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // ============================
    //   FIND BY ID
    // ============================
    @Override
    public Utilisateur findById(Long id) {
        String sql = "SELECT * FROM utilisateur WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ============================
    //   FIND BY EMAIL
    // ============================
    @Override
    public Utilisateur findByEmail(String email) {
        String sql = "SELECT * FROM utilisateur WHERE email = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // ============================
    //   FIND BY USERNAME (login)
    // ============================
    @Override
    public Utilisateur findByUsername(String login) {
        String sql = "SELECT * FROM utilisateur WHERE login = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return map(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Utilisateur authenticate(String login, String password) {
        String sql = "SELECT * FROM utilisateur WHERE login = ? AND password_hash = ? AND actif = TRUE";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    Utilisateur user = map(rs);

                    updateLastLoginDate(user.getId());

                    return user;
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur authentification : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private void updateLastLoginDate(Long userId) {
        String sql = "UPDATE utilisateur SET last_login_date = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================
//   LOGIN EXISTS
// ============================
    @Override
    public boolean loginExists(String login) {
        String sql = "SELECT COUNT(*) as count FROM utilisateur WHERE login = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    // ============================
//   GET USER ROLE
// ============================
    @Override
    public String getUserRole(Long userId) {
        String sql = """
        SELECT r.libelle 
        FROM Role r 
        INNER JOIN Utilisateur_Role ur ON r.id = ur.role_id 
        WHERE ur.utilisateur_id = ? 
        LIMIT 1
    """;

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("libelle");
            }

        } catch (SQLException e) {
            System.err.println(" Erreur récupération rôle : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    // ============================
    //   CREATE
    // ============================
    @Override
    public void create(Utilisateur u) {
        String sql = """
            INSERT INTO utilisateur 
            (creation_date, last_modification_date, created_by, updated_by, nom, prenom, 
             email, tel, adresse, cin, sexe, login, password_hash, last_login_date, 
             date_naissance, actif)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, toTimestamp(u.getCreationDate()));
            pstmt.setTimestamp(2, toTimestamp(u.getLastModificationDate()));
            pstmt.setString(3, u.getCreatedBy());
            pstmt.setString(4, u.getUpdatedBy());
            pstmt.setString(5, u.getNom());
            pstmt.setString(6, u.getPrenom());
            pstmt.setString(7, u.getEmail());
            pstmt.setString(8, u.getTel());
            pstmt.setString(9, u.getAdresse());
            pstmt.setString(10, u.getCin());
            pstmt.setString(11, u.getSexe() != null ? u.getSexe().name() : null);
            pstmt.setString(12, u.getLogin());
            pstmt.setString(13, u.getPasswordHash());
            pstmt.setTimestamp(14, toTimestamp(u.getLastLoginDate()));
            pstmt.setDate(15, toDate(u.getDateNaissance()));
            pstmt.setBoolean(16, u.getActif());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                u.setId(rs.getLong(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================
    //   UPDATE
    // ============================
    @Override
    public void update(Utilisateur u) {
        String sql = """
            UPDATE utilisateur SET
            creation_date = ?, last_modification_date = ?, created_by = ?, updated_by = ?, 
            nom = ?, prenom = ?, email = ?, tel = ?, adresse = ?, cin = ?, sexe = ?, 
            login = ?, password_hash = ?, last_login_date = ?, date_naissance = ?, actif = ?
            WHERE id = ?
        """;

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, toTimestamp(u.getCreationDate()));
            pstmt.setTimestamp(2, toTimestamp(u.getLastModificationDate()));
            pstmt.setString(3, u.getCreatedBy());
            pstmt.setString(4, u.getUpdatedBy());
            pstmt.setString(5, u.getNom());
            pstmt.setString(6, u.getPrenom());
            pstmt.setString(7, u.getEmail());
            pstmt.setString(8, u.getTel());
            pstmt.setString(9, u.getAdresse());
            pstmt.setString(10, u.getCin());
            pstmt.setString(11, u.getSexe() != null ? u.getSexe().name() : null);
            pstmt.setString(12, u.getLogin());
            pstmt.setString(13, u.getPasswordHash());
            pstmt.setTimestamp(14, toTimestamp(u.getLastLoginDate()));
            pstmt.setDate(15, toDate(u.getDateNaissance()));
            pstmt.setBoolean(16, u.getActif());

            pstmt.setLong(17, u.getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================
    //   DELETE
    // ============================
    @Override
    public void delete(Utilisateur u) {
        deleteById(u.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";

        try (Connection conn = Db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ============================
    //   MAP ResultSet → Utilisateur
    // ============================
    private Utilisateur map(ResultSet rs) throws SQLException {
        return Utilisateur.builder()
                .id(rs.getLong("id"))
                .creationDate(toLocalDateTime(rs.getTimestamp("creation_date")))
                .lastModificationDate(toLocalDateTime(rs.getTimestamp("last_modification_date")))
                .createdBy(rs.getString("created_by"))
                .updatedBy(rs.getString("updated_by"))
                .nom(rs.getString("nom"))
                .prenom(rs.getString("prenom"))
                .email(rs.getString("email"))
                .tel(rs.getString("tel"))
                .adresse(rs.getString("adresse"))
                .cin(rs.getString("cin"))
                .sexe(rs.getString("sexe") != null ? ma.dentaluxe.entities.enums.Sexe.valueOf(rs.getString("sexe")) : null)
                .login(rs.getString("login"))
                .passwordHash(rs.getString("password_hash"))
                .lastLoginDate(toLocalDateTime(rs.getTimestamp("last_login_date")))
                .dateNaissance(rs.getDate("date_naissance") != null ? rs.getDate("date_naissance").toLocalDate() : null)
                .actif(rs.getBoolean("actif"))
                .build();
    }

    // ============================
    //   UTILS
    // ============================
    private Timestamp toTimestamp(LocalDateTime dt) {
        return dt != null ? Timestamp.valueOf(dt) : null;
    }

    private LocalDateTime toLocalDateTime(Timestamp ts) {
        return ts != null ? ts.toLocalDateTime() : null;
    }

    private Date toDate(java.time.LocalDate d) {
        return d != null ? Date.valueOf(d) : null;
    }
}