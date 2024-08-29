package com.demo.sqlite.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JWTCoder {

   public static final String ISSUER = "ecommerce";
   public static final long TIME_EXPIRATION = 30 * 60 * 1000; // 30 minutes
   public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
   private static final JwtParser JWT_PARSER = Jwts.parser().verifyWith(SECRET_KEY).build();

   public static Claims parseJWT(String jsonWebToken) {
      return JWT_PARSER.parseSignedClaims(jsonWebToken).getPayload();
   }

   public static String generateJWT(String subject, int userId, List<String> roles) {
      long now = System.currentTimeMillis();
      return Jwts.builder()
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
