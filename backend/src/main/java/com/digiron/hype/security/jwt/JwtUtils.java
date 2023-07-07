package com.digiron.hype.security.jwt;

import com.digiron.hype.security.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.time.Instant.*;
import static java.util.Date.from;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        return ResponseCookie.from(jwtCookie,
                generateTokenFromUsername(userPrincipal.getUsername()))
                .maxAge(24 * 60 * 60)
                .httpOnly(true).build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null).build();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            log.debug("valid JWT token");
            return true;
        } catch (MalformedJwtException e) {
            log.debug("Invalid JWT token", e);
        } catch (ExpiredJwtException e) {
            log.debug("JWT token is expired", e);
        } catch (UnsupportedJwtException e) {
            log.debug("JWT token is unsupported", e);
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty");
        } catch (Exception e) {
            log.error("Unexpected Exception", e);
        }
        log.error("we should never reach this?");
        return false;
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }
}
