package com.lehansun.pet.project.speakingpractice.security;

import com.lehansun.pet.project.speakingpractice.security.exception.JwtAuthenticationException;
import com.lehansun.pet.project.speakingpractice.security.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * An object for creating and verifying JSON Web Tokens
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Service object to get information about Customers.
     */
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String token = jwtTokenProvider.retrieveToken(request);
        if (token == null) {
            chain.doFilter(request, response);
        }

        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

        String token = jwtTokenProvider.retrieveToken(request);
        if (token!=null) {
            String username = jwtTokenProvider.getUsername(token);
            if (username != null) {
                UserDetails details = userDetailsService.loadUserByUsername(username);
                List<GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
                return new UsernamePasswordAuthenticationToken(username, details.getPassword(),  authorities);
            }
        }

    throw  new JwtAuthenticationException("Failed to authorize user");
    }
}
