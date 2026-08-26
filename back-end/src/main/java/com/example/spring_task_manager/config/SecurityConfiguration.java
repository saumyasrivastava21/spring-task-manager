package com.example.spring_task_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var roleAdmin = "ADMIN";
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET,
                                "/api/projects",
                                "/api/tasks")
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users")
                            .hasRole(roleAdmin)
                        .requestMatchers(HttpMethod.POST, "/api/users")
                            .hasRole(roleAdmin)
                        .requestMatchers(HttpMethod.POST,
                                "/api/users/*",
                                "/api/projects/*",
                                "/api/tasks/*")
                            .permitAll()
                        .requestMatchers(HttpMethod.POST,
                                "/api/projects",
                                "/api/tasks")
                            .authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**")
                            .hasRole(roleAdmin)
                        .requestMatchers(HttpMethod.PATCH,
                                "/api/projects/**",
                                "/api/tasks/**")
                            .authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                            .hasRole(roleAdmin)
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/projects",
                                "/api/tasks/**")
                            .authenticated()
                        .anyRequest().authenticated())
                .build();
    }
}
