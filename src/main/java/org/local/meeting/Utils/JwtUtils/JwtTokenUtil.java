package org.local.meeting.Utils.JwtUtils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;


import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifeTime;

    @Value("${jwt.lifetime.refresh}")
    private Duration jwtRefreshTokenLifeTime;

    private SecretKey secretKey;


    @PostConstruct
    private void init() {
        try {
            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("Ошибка инициализации JWT ключа", e);
        }
    }

    public String generateToken(String username) throws Exception {
        Map<String, Object> claims = new HashMap<>();


        claims.put("id",username);

        return builderForToken(claims, jwtLifeTime);

    }
    public String builderForToken(Map<String, Object> claims, Duration time) throws Exception {
        return Jwts.builder()
                .claims(claims)
                .subject(((String) claims.get("id")))// Добавляем дополнительные данные в токен.
                // Указываем subject (имя пользователя).
                .claims().issuedAt(new Date(System.currentTimeMillis())) // Устанавливаем дату создания токена.
                .expiration(new Date(System.currentTimeMillis() + time.toMillis())) // Устанавливаем время истечения токена.
                .and() // Завершаем сборку токена.
                .signWith(secretKey) // Подписываем токен секретным ключом.
                .compact(); // Преобразуем в строковый формат.
    }
    public String generateRefreshToken(String username) throws Exception {
        return builderForToken(Map.of("id",username), jwtRefreshTokenLifeTime);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // Проверяем подпись токена с использованием секретного ключа.
                .build()
                .parseSignedClaims(token) // Парсим claims из токена.
                .getPayload();
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private <Y> Y extractClaim(String token, Function<Claims, Y> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public boolean validateToken(String token, String username) throws Exception {
        final String userName = extractUserName(token);

        return (userName.equals(username) && !isTokenExpired(token));
    }
    public String extractUserName(String token) {

        return extractClaim(token, Claims::getSubject);
    }
}
