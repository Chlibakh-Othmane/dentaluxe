package ma.dentaluxe.service.auth.Impl;

import ma.dentaluxe.service.auth.api.AuthorizationService;
import ma.dentaluxe.service.auth.dto.UserResponse;

public class UserServiceImpl {

    private final AuthorizationService authorizationService;

    public UserServiceImpl(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public UserResponse getUserProfile(Long userId) {
        return authorizationService.getUserById(userId);
    }

    public boolean isEmailTaken(String email) {
        return authorizationService.emailExists(email);
    }
}