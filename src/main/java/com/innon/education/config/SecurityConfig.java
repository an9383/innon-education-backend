package com.innon.education.config;

import com.innon.education.jwt.JwtFilter;
import com.innon.education.jwt.component.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final LogoutHandler logoutHandler;
    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter jwtFilter;
    @Autowired
    private RestAuthenticationEntryPoint jwtEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configure(http))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/favicon.ico","/assets/**","/index.html").permitAll().
                                requestMatchers("/v3/api-docs/**",  "/swagger-ui/**", "/swagger-ui.html", "/ReportingServer/**"
                                        ,"/error","/api/auth/**").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling.authenticationEntryPoint(jwtEntryPoint) //customEntryPoint

                )
                .logout(logout ->
                        logout.logoutUrl("/api/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );
        return http.build();
    }

}