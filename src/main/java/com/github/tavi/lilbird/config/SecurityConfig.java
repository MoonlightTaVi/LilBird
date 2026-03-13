package com.github.tavi.lilbird.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * The configuration for Spring Security.
 * 
 * @version 1.0.0
 */
@Configuration
public class SecurityConfig {

    /**
     * This security filter chain must be used 
     * <b>only for testing purposes</b>.
     * It is automatically enabled when the 'test' profile is used;
     * and deactivated when the 'dev' profile is used.
     * 
     * @param http  The default Spring Security HTTP configuration builder.
     * @return      The SecurityFilterChain that grants public access to all
     *              server end-points.
     */
    @Bean
    @Profile("test")
    public SecurityFilterChain insecureFilterChain(final HttpSecurity http) {
        http
            // All requests are permitted in the test profile
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
            )
            // Default log-in page
            .formLogin(Customizer.withDefaults()); 
        return http.build();
    }
      
    /**
     * The password encoder that is used to encrypt the user credentials
     * before saving them to the database.
     * 
     * @return     {@link BCryptPasswordEncoder}, as of v1.0.0.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}
