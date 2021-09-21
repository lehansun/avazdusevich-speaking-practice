package com.lehansun.pet.project.speakingpractice.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * The Class provides a mechanism for using the SecurityConfigurer
 * and gaining access to configure the SecurityBuilder
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final JwtTokenFilter jwtTokenFilter;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        log.debug("IN configure().");
        builder.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        super.configure(builder);
    }
}
