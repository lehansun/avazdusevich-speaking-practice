package com.lehansun.pet.project.speakingpractice.security;

import com.lehansun.pet.project.speakingpractice.model.dto.PasswordDTO;
import com.lehansun.pet.project.speakingpractice.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Simple security-layer class to work with passwords
 *
 * @author Aliaksei Vazdusevich
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtPasswordValidator {

    /**
     * Service object for encoding passwords
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Service object to get information about Customers.
     */
    private final CustomUserDetailsService userDetailsService;


    public boolean validatePassword(String username, PasswordDTO passwordDTO) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String userDetailsPassword = userDetails.getPassword();
        String rawCurrentPassword = passwordDTO.getCurrentPassword();
        return passwordEncoder.matches(rawCurrentPassword, userDetailsPassword);
    }

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

}
