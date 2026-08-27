package com.example.spring_task_manager.config;

import com.example.spring_task_manager.entity.Position;
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
        var roleManager = Position.MANAGER;
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET,
                                "/api/projects",
                                "/api/tasks")
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users")
                            .hasAuthority(roleManager.getAuthority())
                        .requestMatchers(HttpMethod.POST, "/api/users")
                            .hasAuthority(roleManager.getAuthority())
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
                            .hasAuthority(roleManager.getAuthority())
                        .requestMatchers(HttpMethod.PATCH,
                                "/api/projects/**",
                                "/api/tasks/**")
                            .authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                            .hasAuthority(roleManager.getAuthority())
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
