package com.github.tavi.lilbird.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


/**
 * The configuration for Spring Security.
 * 
 * @version 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${lilbird.admin.name}")
    private String saUsername;
    @Value("${lilbird.admin.password}")
    private String saPassword;


    /**
     * This is a basic {@link SecurityFilterChain} for authentication.
     * 
     * @param http  The default Spring Security HTTP configuration builder.
     * @return      The filter chain that prevent unauthorized access
     *              to non-public end-points.
     */
    @Bean
    @Profile("!test")
    public SecurityFilterChain filterChain(final HttpSecurity http) {
        http
            // CSRF are disabled
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Admin end-points
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")
                // Developer end-points (for convenience they are separated)
                .requestMatchers("/dev/**")
                .hasRole("ADMIN")
                // Usual user end-points
                .requestMatchers("/api/user/**")
                .authenticated()
                // Default is public
                .anyRequest().permitAll()
            )
            // Default log-in page
            .formLogin(Customizer.withDefaults()); 
        return http.build();
    }

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
    
    @Bean
    public UserDetailsService userDetailsService(final PasswordEncoder encoder) {
        // TODO Make persistant user details service
        final UserDetails user = User
                .builder()
                .username(saUsername)
                .password(saPassword)
                .roles("ADMIN")
                .passwordEncoder(password -> encoder.encode(password))
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
