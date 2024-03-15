package com.rucha.PasswordManager.impl;

import com.rucha.PasswordManager.constants.JWTConstants;
import com.rucha.PasswordManager.entity.User;
import com.rucha.PasswordManager.vo.LoginUserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JWTServiceImpl {

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        System.out.println(claims.get("userId"));
        return (Long) claims.get("userId");
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        boolean status = extractExpiration(token).before(new Date()) ||
                extractExpiration(token).before(new Date(System.currentTimeMillis() +
                        TimeUnit.MINUTES.toMillis(5)));
        return status;
    }

    public Boolean validateToken(String token, User userDetails) {
        final String username = extractUsername(token);
        if (isTokenExpired(token)){
            generateToken(userDetails);
        }
        return (username.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }

    public Map<String,String> generateToken(User user){
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user);
    }

    private Map<String,String> createToken(Map<String, Object> claims, User user) {
        Map<String, String> userToken = new HashMap<>();
        Date expiryDate = Date.from(ZonedDateTime.now().plusMinutes(JWTConstants.JWT_DEFAULT_TIME).toInstant());
        claims.put("userId", user.getId());
        claims.put("userEmail", user.getEmail());
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiryDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        userToken.put(user.getEmail(), token);
        return userToken;
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWTConstants.JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
