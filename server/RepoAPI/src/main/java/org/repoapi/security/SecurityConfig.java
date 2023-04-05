package org.repoapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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
                .requestMatchers(HttpMethod.POST, "/repo/account/login").permitAll()
//                .requestMatchers(HttpMethod.GET, "/auth/google").permitAll()
                )
                .addFilter(new JwtRequestFilter(authenticationManager, jwtConverter))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
//                .oauth2ResourceServer().jwt()
//                .and()
//                .and()
                .cors(c->corsConfigurationSource())
                .oauth2Login()
                    .defaultSuccessUrl("/", true)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();

    }

    @Bean
    protected AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

