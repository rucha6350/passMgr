package com.rucha.PasswordManager.helper;

import com.rucha.PasswordManager.constants.JWTConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieHelper {
    public ResponseCookie setCookie(String token){
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(JWTConstants.JWT_DEFAULT_TIME * 60)
                .build();
    }

    public String getToken(HttpServletRequest request){
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("accessToken"))
                    .findFirst()
                    .map(Cookie::getValue).orElse(null);
        }
        return null;
    }
}
