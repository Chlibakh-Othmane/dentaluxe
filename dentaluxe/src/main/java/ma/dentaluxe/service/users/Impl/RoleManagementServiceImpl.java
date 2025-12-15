package ma.dentaluxe.service.users.Impl;

import ma.dentaluxe.service.users.api.RoleManagementService;
import ma.dentaluxe.service.users.dto.*;
import ma.dentaluxe.entities.utilisateur.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RoleManagementServiceImpl implements RoleManagementService {

    private final Map<Long, Utilisateur> utilisateurs = new ConcurrentHashMap<>();
    private final Map<Long, Admin> admins = new ConcurrentHashMap<>();
    private final Map<Long, Medecin> medecins = new ConcurrentHashMap<>();
    private final Map<Long, Secretaire> secretaires = new ConcurrentHashMap<>();
    private final Map<String, Long> emailToUserId = new ConcurrentHashMap<>();

    private final AtomicLong userIdGenerator = new AtomicLong(1);

    @Override
    public UserAccountDto createAdmin(CreateAdminRequest request) {
        if (emailToUserId.containsKey(request.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        Long userId = userIdGenerator.getAndIncrement();
        LocalDateTime now = LocalDateTime.now();

        Utilisateur utilisateur = Utilisateur.builder()
                .id(userId)
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .tel(request.getTel())
                .adresse(request.getAdresse())
                .cin(request.getCin())
                .sexe(request.getSexe())
                .login(request.getLogin())
                .passwordHash(hashPassword(request.getPassword()))
                .dateNaissance(request.getDateNaissance())
                .actif(true)
                .creationDate(now)
                .lastModificationDate(now)
                .createdBy("SYSTEM")
                .build();

        Admin admin = Admin.builder()
                .id(userId)
                .utilisateur(utilisateur)
                .build();

        utilisateurs.put(userId, utilisateur);
        admins.put(userId, admin);
        emailToUserId.put(request.getEmail(), userId);

        return toUserAccountDto(utilisateur, "ADMIN", null, null);
    }

    @Override
    public UserAccountDto createMedecin(CreateMedecinRequest request) {
        if (emailToUserId.containsKey(request.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        Long userId = userIdGenerator.getAndIncrement();
        LocalDateTime now = LocalDateTime.now();

        Utilisateur utilisateur = Utilisateur.builder()
                .id(userId)
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .tel(request.getTel())
                .adresse(request.getAdresse())
                .cin(request.getCin())
                .sexe(request.getSexe())
                .login(request.getLogin())
                .passwordHash(hashPassword(request.getPassword()))
                .dateNaissance(request.getDateNaissance())
                .actif(true)
                .creationDate(now)
                .lastModificationDate(now)
                .createdBy("SYSTEM")
                .build();

        Medecin medecin = Medecin.builder()
                .id(userId)
                .specialite(request.getSpecialite())
                .agendaMedecin(request.getAgendaMedecin())
                .build();

        utilisateurs.put(userId, utilisateur);
        medecins.put(userId, medecin);
        emailToUserId.put(request.getEmail(), userId);

        return toUserAccountDto(utilisateur, "MEDECIN", medecin, null);
    }

    @Override
    public UserAccountDto createSecretaire(CreateSecretaireRequest request) {
        if (emailToUserId.containsKey(request.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        Long userId = userIdGenerator.getAndIncrement();
        LocalDateTime now = LocalDateTime.now();

        Utilisateur utilisateur = Utilisateur.builder()
                .id(userId)
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .tel(request.getTel())
                .adresse(request.getAdresse())
                .cin(request.getCin())
                .sexe(request.getSexe())
                .login(request.getLogin())
                .passwordHash(hashPassword(request.getPassword()))
                .dateNaissance(request.getDateNaissance())
                .actif(true)
                .creationDate(now)
                .lastModificationDate(now)
                .createdBy("SYSTEM")
                .build();

        Secretaire secretaire = Secretaire.builder()
                .id(userId)
                .numCnss(request.getNumCnss())
                .commission(request.getCommission())
                .build();

        utilisateurs.put(userId, utilisateur);
        secretaires.put(userId, secretaire);
        emailToUserId.put(request.getEmail(), userId);

        return toUserAccountDto(utilisateur, "SECRETAIRE", null, secretaire);
    }

    @Override
    public void deactivateUser(Long userId) {
        Utilisateur utilisateur = utilisateurs.get(userId);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        utilisateur.setActif(false);
        utilisateur.setLastModificationDate(LocalDateTime.now());
    }

    @Override
    public void activateUser(Long userId) {
        Utilisateur utilisateur = utilisateurs.get(userId);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        utilisateur.setActif(true);
        utilisateur.setLastModificationDate(LocalDateTime.now());
    }

    @Override
    public boolean hasRole(Long userId, String role) {
        switch (role.toUpperCase()) {
            case "ADMIN":
                return admins.containsKey(userId);
            case "MEDECIN":
                return medecins.containsKey(userId);
            case "SECRETAIRE":
                return secretaires.containsKey(userId);
            default:
                return false;
        }
    }

    public Utilisateur getUtilisateur(Long userId) {
        return utilisateurs.get(userId);
    }

    public String getRoleByUserId(Long userId) {
        if (admins.containsKey(userId)) return "ADMIN";
        if (medecins.containsKey(userId)) return "MEDECIN";
        if (secretaires.containsKey(userId)) return "SECRETAIRE";
        return null;
    }

    public Medecin getMedecin(Long userId) {
        return medecins.get(userId);
    }

    public Secretaire getSecretaire(Long userId) {
        return secretaires.get(userId);
    }

    public Map<Long, Medecin> getAllMedecins() {
        return new ConcurrentHashMap<>(medecins);
    }

    public Map<Long, Secretaire> getAllSecretaires() {
        return new ConcurrentHashMap<>(secretaires);
    }

    public Map<Long, Utilisateur> getAllUtilisateurs() {
        return new ConcurrentHashMap<>(utilisateurs);
    }

    public Long getUserIdByEmail(String email) {
        return emailToUserId.get(email);
    }

    private String hashPassword(String password) {
        return "HASHED_" + password;
    }

    private UserAccountDto toUserAccountDto(Utilisateur u, String role, Medecin m, Secretaire s) {
        UserAccountDto dto = new UserAccountDto();
        dto.setId(u.getId());
        dto.setNom(u.getNom());
        dto.setPrenom(u.getPrenom());
        dto.setEmail(u.getEmail());
        dto.setTel(u.getTel());
        dto.setAdresse(u.getAdresse());
        dto.setCin(u.getCin());
        dto.setSexe(u.getSexe());
        dto.setLogin(u.getLogin());
        dto.setDateNaissance(u.getDateNaissance());
        dto.setActif(u.getActif());
        dto.setCreationDate(u.getCreationDate());
        dto.setLastLoginDate(u.getLastLoginDate());
        dto.setRole(role);

        if (m != null) {
            dto.setSpecialite(m.getSpecialite());
            dto.setAgendaMedecin(m.getAgendaMedecin());
        }

        if (s != null) {
            dto.setNumCnss(s.getNumCnss());
            dto.setCommission(s.getCommission());
        }

        return dto;
    }
}