package ch.heigvd.gamification.util;

public class JWTUtils {
    public static final String SCHEMA = "Bearer";

    public static boolean checkJWTHeader(String header) {
        return header != null && header.startsWith(String.format("%s ", SCHEMA));
    }

    public static String getJWTValue(String header) {
        return header.substring(SCHEMA.length() + 1);
    }
}
