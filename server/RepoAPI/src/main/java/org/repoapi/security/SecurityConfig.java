package org.repoapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final JwtConverter jwtConverter;
    public SecurityConfig(JwtConverter jwtConverter){
        this.jwtConverter = jwtConverter;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf().disable();

        http.cors();

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/repo/account").permitAll()
                .requestMatchers(HttpMethod.PUT, "/repo/account").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/repo/account").authenticated()
                .requestMatchers(HttpMethod.POST, "/repo/account/login").permitAll())
                .addFilter(new JwtRequestFilter(authenticationManager, jwtConverter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();

    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}

