package com.pickle.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {
    
    @Value("${jwt.secret:}")
    private String secret;
    
    @Value("${jwt.expire:604800000}")
    private Long expire; // 默认7天
    
    private SecretKey getSecretKey() {
//        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }
    
    public String generateToken(String openId, String userId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire);
        
        return Jwts.builder()
                .setSubject(userId)
                .claim("openId", openId)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("解析token失败", e);
            return null;
        }
    }
    
    public boolean validateToken(String token) {
        Claims claims = parseToken(token);
        return claims != null && claims.getExpiration().after(new Date());
    }
}