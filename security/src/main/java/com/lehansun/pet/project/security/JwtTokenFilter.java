package com.lehansun.pet.project.security;

import com.lehansun.pet.project.security.exception.JwtAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Simple filter-class for authentication
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    /**
     * Short message that the token is not valid.
     */
    public static final String TOKEN_IS_EXPIRED_OR_INVALID = "JWT token is expired or invalid";

    /**
     * An object for creating and verifying JSON Web Tokens
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Checks user authentication
     *
     * @param request Servlet request
     * @param response Servlet response
     * @param filterChain Filter chain
     *
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.debug("IN doFilter()");
        String token = jwtTokenProvider.retrieveToken((HttpServletRequest) request);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                log.debug("IN doFilter(). Request is authenticated.");
            } else {
                log.warn("IN doFilter(). Failed to retrieve token from request.");
            }
        } catch (JwtAuthenticationException e) {
            log.warn("Failed to filter request. {}.", e.getLocalizedMessage());
            SecurityContextHolder.clearContext();
            ((HttpServletResponse) response).sendError(e.getHttpStatus().value());
            throw new JwtAuthenticationException(TOKEN_IS_EXPIRED_OR_INVALID, HttpStatus.FORBIDDEN);
        }
        filterChain.doFilter(request, response);
    }
}
