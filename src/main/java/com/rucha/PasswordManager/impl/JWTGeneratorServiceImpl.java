//package com.rucha.PasswordManager.impl;
//
//import com.rucha.PasswordManager.constants.JWTConstants;
//import com.rucha.PasswordManager.entity.User;
//import com.rucha.PasswordManager.service.JWTGeneratorService;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.time.ZonedDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JWTGeneratorServiceImpl implements JWTGeneratorService {
//
//    @Override
//    public Map<String, String> generateToken(User user) {
//        String jwtToken="";
//        jwtToken = Jwts.builder()
//                .setSubject(user.getEmail())
//                .setIssuedAt(new Date())
//                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(JWTConstants.JWT_DEFAULT_TIME).toInstant()))
//                .signWith(SignatureAlgorithm.HS256, JWTConstants.JWT_SECRET)
//                .compact();
//        Map<String, String> jwtTokenGen = new HashMap<>();
//        jwtTokenGen.put("token", jwtToken);
//        jwtTokenGen.put("message", "Login Successful");
//        return jwtTokenGen;
//    }
//}
