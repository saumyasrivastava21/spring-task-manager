package com.example.spring_task_manager.config;

import com.example.spring_task_manager.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
                .formLogin(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

}
