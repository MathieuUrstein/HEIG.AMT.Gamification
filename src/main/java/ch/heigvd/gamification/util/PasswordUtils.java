package ch.heigvd.gamification.util;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

public class PasswordUtils {
    public static final int SALT_LENGTH = 32;
    public static final int N_ITERATIONS = 5000;
    public static final int KEY_LENGTH = 256;
    public static final String HASHING_ALGORITHM = "PBKDF2WithHmacSHA512";

    private static final Random random = new SecureRandom();

    public static byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    public static boolean isPasswordValid(String password, byte[] storedPassword, byte[] storedSalt) {
        return Arrays.equals(hashPassword(password, storedSalt), storedPassword);
    }

    /**
     * Hashes and salts the given password.
     *
     * Based on https://www.owasp.org/index.php/Hashing_Java.
     *
     * @param password The password to hash.
     * @param salt The salt to use. Should be randomly data and vary for each user.
     * @return The hashed and salted password.
     */
    public static byte[] hashPassword(String password, byte[] salt) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance(HASHING_ALGORITHM);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, N_ITERATIONS, KEY_LENGTH);
            SecretKey key = skf.generateSecret(spec);
            return key.getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }
}
