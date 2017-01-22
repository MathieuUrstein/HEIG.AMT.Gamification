package ch.heigvd.gamification.util;

import ch.heigvd.gamification.GamificationApplication;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JWTUtils {
    private static final Logger LOG = Logger.getLogger(JWTUtils.class.getName());

    private static String SCHEMA;
    private static String ISSUER;
    private static String SECRET_KEY;

    private static JWTVerifier verifier;

    static {
        try (InputStream input = GamificationApplication.class.getClassLoader().getResourceAsStream("JWT.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            SCHEMA = prop.getProperty("SCHEMA");
            ISSUER = prop.getProperty("ISSUER");
            SECRET_KEY = prop.getProperty("SECRET_KEY");

            verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                    .withIssuer(ISSUER)
                    .build();

        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    public static String extractToken(String header) {
        if (header == null || header.length() < SCHEMA.length() + 1) {
            return null;
        }

        return header.substring(SCHEMA.length() + 1);
    }

    public static String generateToken(String subject) {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(subject)
                    .sign(Algorithm.HMAC256(SECRET_KEY));
        }
        catch (JWTCreationException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            return verifier.verify(token);
        } catch (JWTVerificationException exception){
            return null;
        }
    }
}
