package dev.loskutnikov.onlinelibrary.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

import java.util.Date;

@Component
public class JwtTokenManager {

    private final SecretKey key;

    private final long expiretionTime;

    public JwtTokenManager(
            @Value("${jwt.secret-key}") String keyString,
            @Value("${jwt.lifetime}") long expiretionTime) {
        this.key = Keys.hmacShaKeyFor(keyString.getBytes());
        this.expiretionTime = expiretionTime;
    }

    public String generateToken(String login) {

        return Jwts
                .builder()
                .subject(login)
                .signWith(key)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiretionTime))
                .compact();
    }

    public String getLoginFromToken(String jwt) {
        return Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

}
