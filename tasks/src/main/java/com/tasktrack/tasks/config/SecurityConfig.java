package com.tasktrack.tasks.config;

import com.tasktrack.tasks.domain.auth.repository.TokenRepository;
import com.tasktrack.tasks.domain.auth.repository.UserRepository;
import com.tasktrack.tasks.domain.auth.service.CustomOauth2UserService;
import com.tasktrack.tasks.domain.auth.service.CustomSuccessHandler;
import com.tasktrack.tasks.domain.auth.service.JWTFilter;
import com.tasktrack.tasks.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomOauth2UserService customOauth2UserService;

    private final CustomSuccessHandler customSuccessHandler;

    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;
    public SecurityConfig(CustomOauth2UserService customOauth2UserService, CustomSuccessHandler customSuccessHandler,
                          JWTUtil jwtUtil, UserRepository userRepository, TokenRepository tokenRepository) {
        this.customOauth2UserService = customOauth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter(jwtUtil, userRepository, tokenRepository);
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.logout(AbstractHttpConfigurer::disable);

        http.cors(corsCustomizer ->
                corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));
        
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        http.oauth2Login((auth) -> auth
                .userInfoEndpoint((uie) -> uie
                        .userService(customOauth2UserService))
                .successHandler(customSuccessHandler));

        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/", "/api/util/**").permitAll()
                .anyRequest().authenticated());

        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint(((request, response,
                                                     authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                })));

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
