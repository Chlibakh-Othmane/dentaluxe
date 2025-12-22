package ma.dentaluxe.common.utilitaire;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^0[5-7][0-9]{8}$"; // Format marocain standard

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return Pattern.matches(PHONE_REGEX, phone);
    }
}