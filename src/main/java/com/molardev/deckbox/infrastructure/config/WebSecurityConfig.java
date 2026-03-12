package com.molardev.deckbox.infrastructure.config;

import com.molardev.deckbox.infrastructure.authentication.TokenService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.molardev.deckbox.infrastructure.authentication.JwtAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig { // Made Public

  private final TokenService tokenService;

  WebSecurityConfig(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Added throws Exception
    System.out.println("--- SECURITY CONFIG ACTIVE ---");

    http
        .addFilterBefore(
            new JwtAuthenticationFilter(tokenService),
            UsernamePasswordAuthenticationFilter.class)
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll() // Added /api/ to match your structure
            .anyRequest().authenticated())
        // We removed the H2 permitAll here because we are using the "Ignoring" bean
        // below
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    // This TELLS Spring to stay away from H2 entirely. No bouncer, no login box.
    return (web) -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}