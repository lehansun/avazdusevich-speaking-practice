package com.lehansun.pet.project.speakingpractice.security;

import com.lehansun.pet.project.speakingpractice.security.exception.JwtAuthenticationException;
import com.lehansun.pet.project.speakingpractice.security.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains methods for creating and verifying
 * JSON Web Tokens using JJWT library.
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final String TOKEN_IS_EXPIRED_OR_INVALID = "JWT token is expired or invalid";

    /**
     * A secret key to compute the signature of JWT.
     */
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * The name of the HTTP header used to transfer the token
     */
    @Value("${jwt.header}")
    private String authorizationHeader;

    /**
     * Token validity time in milliseconds
     */
    @Value("${jwt.expiration}")
    private Long validityInSeconds;


    /**
     * Service object to get information about Customers.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Init-method for establishing the secret key
     */
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    /**
     * Creates JSON Web Token
     *
     * @param username username of authenticated customer
     * @param roles roles of authenticated customer
     * @return JSON Web Token
     */
    public String createToken(String username, Set<String> roles) {
        log.debug("IN createToken(). Trying to create token for user: {}.", username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds * 1000);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .claim("roles", String.join(",", roles))
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("\tIN createToken(). Token is ready: {}.", token);
        return token;
    }

    /**
     * Checks a token for validity
     *
     * @param token JSON Web Token
     * @return true if token is valid and false otherwise.
     */
    public boolean validateToken(String token) {
        log.debug("IN validateToken(). Trying to validate token: " + token);
        try {
            boolean isSigned = Jwts.parser().setSigningKey(secretKey).isSigned(token);
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            boolean isValid = isSigned && !isExpired(claimsJws);
            log.debug("\tIN validateToken(). Is token valid: {}.", isValid);
            return isValid;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("\tIN validateToken(). Failed to validate token.");
            throw new JwtAuthenticationException(TOKEN_IS_EXPIRED_OR_INVALID, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Creates new Authentication.
     *
     * @param token JSON Web Token
     * @return new Authentication.
     */
    public Authentication getAuthentication(String token) {
        log.debug("IN getAuthentication().  Trying to get Authentication from token: {}.", token);
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * Retrieves username from token
     *
     * @param token JSON Web Token
     * @return username
     */
    public String getUsername(String token) {
        log.debug("IN getUsername(). Trying to get Username from token: {}", token);
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Retrieves authorities from token
     *
     * @param token JSON Web Token
     * @return List of GrantedAuthorities
     */
    public List<GrantedAuthority> getAuthorities(String token) {
        log.debug("IN getAuthorities(). Trying to get Username from token: {}", token);
        String roles = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("roles", String.class);
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    /**
     * Retrieves token from HttpServletRequest
     *
     * @param request HTTP request
     * @return JSON Web Token
     */
    public String retrieveToken(HttpServletRequest request) {
        log.debug("IN retrieveToken().");
        return request.getHeader(authorizationHeader);
    }

    /**
     * Checks the token is expired
     *
     * @param claimsJws JSON Web Token claims
     * @return true if token is expired and false otherwise.
     */
    private boolean isExpired(Jws<Claims> claimsJws) {
        log.debug("IN isNotExpired().");
        Date expiration = claimsJws.getBody().getExpiration();
        Date issuedAt = claimsJws.getBody().getIssuedAt();
        Date now = new Date();
        long duration = (expiration.getTime() - issuedAt.getTime()) / 1000;
        return issuedAt.after(now) && expiration.before(now) && duration != validityInSeconds;

    }

}
