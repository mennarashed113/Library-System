package com.library.library_system.config;

import com.library.library_system.repository.UserRepository;
import com.library.library_system.services.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                configurer
                        // Auth and User endpoints (registration/login allowed to all)

                        .requestMatchers(HttpMethod.POST, "/api/users/createUsers").hasRole("ADMIN")
                        // Book endpoints
                        .requestMatchers(HttpMethod.GET, "/api/books/**").hasAnyRole( "LIBRARIAN", "STAFF", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/books").hasAnyRole("LIBRARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasAnyRole("LIBRARIAN", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                        // Borrowing endpoints
                        .requestMatchers(HttpMethod.POST, "/api/borrowings").hasRole("STAFF")
                        .requestMatchers(HttpMethod.POST, "/api/borrowings/return").hasRole("STAFF")
                        .requestMatchers(HttpMethod.GET, "/api/borrowings/**").hasAnyRole("LIBRARIAN", "ADMIN")

                        // Member endpoints
                        .requestMatchers("/api/members/**").hasAnyRole("LIBRARIAN", "ADMIN")
                        .requestMatchers( "/api/users/login").permitAll()
                        // Default rule
                        .anyRequest().authenticated()
        );

        // Use HTTP Basic authentication
        http.httpBasic(Customizer.withDefaults());

        // Disable CSRF for REST API
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

}
