package com.lehansun.pet.project.security;

import com.lehansun.pet.project.security.exception.JwtAuthenticationException;
import com.lehansun.pet.project.security.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final String TOKEN_IS_EXPIRED_OR_INVALID = "JWT token is expired or invalid";

    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${jwt.expiration}")
    private Long validityInMilliseconds;

    private final CustomUserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, String role) {
        log.debug("IN createToken(). Trying to create token for user: {}.", username);
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("role", role);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds * 1000);
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("\tIN createToken(). Token is ready: {}.", token);
        return token;
    }

    public boolean validateToken(String token) {
        log.debug("IN validateToken(). Trying to validate token: " + token);
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            boolean isValid = !claimsJws.getBody().getExpiration().before(new Date());
            log.debug("\tIN validateToken(). Is token valid: {}.", isValid);
            return isValid;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("\tIN validateToken(). Failed to validate token.");
            throw new JwtAuthenticationException(TOKEN_IS_EXPIRED_OR_INVALID, HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthentication(String token) {
        log.debug("IN getAuthentication().  Trying to get Authentication from token: {}.", token);
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        log.debug("IN getUsername(). Trying to get Username from token: {}", token);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        log.debug("IN resolveToken().  Trying to Resolve token from: {}", request);
        log.debug("\tIN resolveToken().  Resolving by header: {}.", authorizationHeader);
        String token = request.getHeader(authorizationHeader);
        log.debug("\tIN resolveToken().  Resolved token: {}.", token);
        return token;
    }

}
