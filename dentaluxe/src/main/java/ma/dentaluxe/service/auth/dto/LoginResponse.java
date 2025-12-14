package ma.dentaluxe.service.auth.dto;

public class LoginResponse {
    private Long userId;
    private String email;
    private String username;
    private String sessionId;
    private String message;

    public LoginResponse() {}

    public LoginResponse(Long userId, String email, String username, String sessionId) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.sessionId = sessionId;
        this.message = "Login successful";
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}