package ch.heigvd.gamification.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;

public class JWTUtils {
    public static final String SCHEMA = "Bearer";
    public static final String ISSUER = "auth0";

    // The secret key to use. Should have been in a config file.
    public static final String SECRET_KEY = "gamification";

    private static JWTVerifier verifier;

    private static void createJWTVerifierIfNotExisting() {
        if (verifier == null) {
            try {
                verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                        .withIssuer(ISSUER)
                        .build();
            }
            catch (UnsupportedEncodingException e) {
                System.err.println("Could not create JWTVerifier instance.");
                e.printStackTrace();
            }
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
        createJWTVerifierIfNotExisting();
        try {
            return verifier.verify(token);
        }
        catch (JWTVerificationException exception){
            return null;
        }
    }
}
