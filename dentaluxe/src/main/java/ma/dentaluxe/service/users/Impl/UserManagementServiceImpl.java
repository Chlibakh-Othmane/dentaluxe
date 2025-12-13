package ma.dentaluxe.service.users.Impl;

import ma.dentaluxe.service.users.api.UserManagementService;
import ma.dentaluxe.service.users.dto.UpdateUserProfileRequest;
import ma.dentaluxe.service.users.dto.UserAccountDto;
import ma.dentaluxe.entities.utilisateur.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserManagementServiceImpl implements UserManagementService {

    private final RoleManagementServiceImpl roleManagementService;

    public UserManagementServiceImpl(RoleManagementServiceImpl roleManagementService) {
        this.roleManagementService = roleManagementService;
    }

    @Override
    public UserAccountDto getUserById(Long userId) {
        Utilisateur utilisateur = roleManagementService.getUtilisateur(userId);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        String role = roleManagementService.getRoleByUserId(userId);
        Medecin medecin = roleManagementService.getMedecin(userId);
        Secretaire secretaire = roleManagementService.getSecretaire(userId);

        return toUserAccountDto(utilisateur, role, medecin, secretaire);
    }

    @Override
    public UserAccountDto getUserByEmail(String email) {
        Long userId = roleManagementService.getUserIdByEmail(email);
        if (userId == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }
        return getUserById(userId);
    }

    @Override
    public List<UserAccountDto> getAllActiveUsers() {
        List<UserAccountDto> activeUsers = new ArrayList<>();
        Map<Long, Utilisateur> allUsers = roleManagementService.getAllUtilisateurs();

        for (Map.Entry<Long, Utilisateur> entry : allUsers.entrySet()) {
            Utilisateur u = entry.getValue();
            if (u.getActif()) {
                Long userId = entry.getKey();
                String role = roleManagementService.getRoleByUserId(userId);
                Medecin medecin = roleManagementService.getMedecin(userId);
                Secretaire secretaire = roleManagementService.getSecretaire(userId);

                activeUsers.add(toUserAccountDto(u, role, medecin, secretaire));
            }
        }

        return activeUsers;
    }

    @Override
    public List<UserAccountDto> getAllMedecins() {
        List<UserAccountDto> medecinsList = new ArrayList<>();
        Map<Long, Medecin> allMedecins = roleManagementService.getAllMedecins();

        for (Map.Entry<Long, Medecin> entry : allMedecins.entrySet()) {
            Long userId = entry.getKey();
            Utilisateur u = roleManagementService.getUtilisateur(userId);
            if (u != null) {
                medecinsList.add(toUserAccountDto(u, "MEDECIN", entry.getValue(), null));
            }
        }

        return medecinsList;
    }

    @Override
    public List<UserAccountDto> getAllSecretaires() {
        List<UserAccountDto> secretairesList = new ArrayList<>();
        Map<Long, Secretaire> allSecretaires = roleManagementService.getAllSecretaires();

        for (Map.Entry<Long, Secretaire> entry : allSecretaires.entrySet()) {
            Long userId = entry.getKey();
            Utilisateur u = roleManagementService.getUtilisateur(userId);
            if (u != null) {
                secretairesList.add(toUserAccountDto(u, "SECRETAIRE", null, entry.getValue()));
            }
        }

        return secretairesList;
    }

    @Override
    public UserAccountDto updateUserProfile(Long userId, UpdateUserProfileRequest request) {
        Utilisateur utilisateur = roleManagementService.getUtilisateur(userId);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        if (request.getNom() != null) {
            utilisateur.setNom(request.getNom());
        }
        if (request.getPrenom() != null) {
            utilisateur.setPrenom(request.getPrenom());
        }
        if (request.getTel() != null) {
            utilisateur.setTel(request.getTel());
        }
        if (request.getAdresse() != null) {
            utilisateur.setAdresse(request.getAdresse());
        }
        if (request.getSexe() != null) {
            utilisateur.setSexe(request.getSexe());
        }
        if (request.getDateNaissance() != null) {
            utilisateur.setDateNaissance(request.getDateNaissance());
        }

        utilisateur.setLastModificationDate(LocalDateTime.now());
        utilisateur.setUpdatedBy("SYSTEM");

        String role = roleManagementService.getRoleByUserId(userId);
        Medecin medecin = roleManagementService.getMedecin(userId);
        Secretaire secretaire = roleManagementService.getSecretaire(userId);

        return toUserAccountDto(utilisateur, role, medecin, secretaire);
    }

    @Override
    public void deleteUser(Long userId) {
        Utilisateur utilisateur = roleManagementService.getUtilisateur(userId);
        if (utilisateur == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        roleManagementService.deactivateUser(userId);
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