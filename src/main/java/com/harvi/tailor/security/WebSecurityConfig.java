package com.harvi.tailor.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

  private final AuthenticationEntryPointImpl authenticationEntryPoint;
  private final UserDetailsService userDetailsServiceImpl;
  private final AuthFilter authFilter;

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsServiceImpl);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

    // CORS needs to be processed before Spring Web Security
    // Besides, it exclude OPTIONS request from authorization checks
    httpSecurity
        .cors(cors -> {})
        // We don't need CSRF for this app
        .csrf(csrf -> csrf.disable())
        // dont authenticate login request (i.e. /authenticate)
        .authorizeHttpRequests(
            authz ->
                authz
                    .requestMatchers("/authenticate")
                    .permitAll()
                    // all other requests need to be authenticated
                    .anyRequest()
                    .authenticated())
        // configure exceptionHandling using AuthenticationEntryPoint
        .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
        // make sure we use stateless session; session won't be used to store user's
        // state.
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Configure authentication provider
        .authenticationProvider(authenticationProvider());

    // Add a filter to validate the tokens with every request
    httpSecurity.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }
}
