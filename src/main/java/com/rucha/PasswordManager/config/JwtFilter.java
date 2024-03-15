//package com.rucha.PasswordManager.config;
//
//import com.rucha.PasswordManager.constants.JWTConstants;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.ZonedDateTime;
//import java.util.Date;
//import java.util.concurrent.TimeUnit;
//
//public class JwtFilter extends GenericFilter {
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
//                         FilterChain filterChain) throws IOException, ServletException {
//        final HttpServletRequest request = (HttpServletRequest) servletRequest;
//        final HttpServletResponse response = (HttpServletResponse) servletResponse;
//        final String authHeader = request.getHeader("authorization");
//        if ("OPTIONS".equals(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            filterChain.doFilter(request, response);
//        } else {
//            if(authHeader == null || !authHeader.startsWith("Bearer ")){
//                throw new ServletException("An exception occurred");
//            }
//        }
//        final String token = authHeader.substring(7);
//        Claims claims = checkIfTokenExpired(token);
//        request.setAttribute("claims", claims);
//        request.setAttribute("blog", servletRequest.getParameter("id"));
//        filterChain.doFilter(request, response);
//    }
//
//    public Claims checkIfTokenExpired(String token){
//        Claims claims = Jwts
//                .parser()
//                .setSigningKey(JWTConstants.JWT_SECRET)
//                .parseClaimsJws(token)
//                .getBody();
//        Date jwtExpiration = claims.getExpiration();
//        Date currentTime = new Date();
//        Date checkExpiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
//        //Condition when JWT needs to be refreshed - when Exp time is less than current time and
//        //if jwt is going to expire after 5 minutes
//        if (jwtExpiration.before(currentTime) || jwtExpiration.before(checkExpiration)){
//            claims.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(JWTConstants.JWT_DEFAULT_TIME).toInstant()));
//        }
//        return claims;
//    }
//}
