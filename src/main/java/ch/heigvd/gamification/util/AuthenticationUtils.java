package ch.heigvd.gamification.util;

public class AuthenticationUtils {

    public static boolean isPasswordValid(String password, String passwordHashed) {
        // FIXME check with hashed password
        return password.equals(passwordHashed);
    }
}
