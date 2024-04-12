package com.demo.sqlite.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JWTCoder {

    public static final String ISSUER = "ecommerce";
    public static final long TIME_EXPIRATION = 30 * 60 * 1000; // 30 minutes
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();


    public static Claims parseJWT(String jsonWebToken) {
        return
                Jwts.parser()
                        .verifyWith(SECRET_KEY)
                        .build()
                        .parseSignedClaims(jsonWebToken)
                        .getPayload();
    }

    public static String generateJWT(String subject, List<String> roles, int userId) {
        long now = System.currentTimeMillis();
        return
                Jwts.builder()
                        .id(ISSUER)
                        .subject(subject)
                        .claims(Map.of("userId", userId))
                        .claim("roles", roles)
                        .issuedAt(new Date(now))
                        .expiration(new Date(now + TIME_EXPIRATION))
                        .signWith(SECRET_KEY)
                        .compact();
    }

}
