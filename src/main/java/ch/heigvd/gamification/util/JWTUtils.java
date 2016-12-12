package ch.heigvd.gamification.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.io.UnsupportedEncodingException;

public class JWTUtils {
    public static final String SCHEMA = "Bearer";
    public static final String ISSUER = "auth0";
    public static final String SECRET_KEY = "gamification"; // FIXME temporary

    private static JWTVerifier verifier;

    private static void createJWTVerifierIfNotExisting() {
        if (verifier == null) {
            try {
                verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY))
                        .withIssuer(ISSUER)
                        .build();
            } catch (UnsupportedEncodingException e) {
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

    public static String generateToken() {
        try {
            return JWT.create()
                    .withIssuer(ISSUER)
                    .sign(Algorithm.HMAC256(SECRET_KEY));
        } catch (JWTCreationException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isTokenValid(String token) {
        createJWTVerifierIfNotExisting();
        try {
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            return false;
        }
    }
}
