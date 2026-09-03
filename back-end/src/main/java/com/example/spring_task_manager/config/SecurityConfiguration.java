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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationConverter jwtAuthenticationConverter) throws Exception {
        var roleManager = Position.MANAGER;
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET,
                                "/api/projects",
                                "/api/tasks")
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/*")
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
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .oidcUserService(oidcUserService())
                        )
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(16);
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {


            List<String> roles = jwt.getClaimAsStringList("auth_roles");

            if (roles == null) {
                return Collections.emptyList();
            }

            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .toList();
        });

        return converter;
    }

    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            OidcUserService delegate = new OidcUserService();
            OidcUser oidcUser = delegate.loadUser(userRequest);

            Set<GrantedAuthority> authorities =
                    new HashSet<>(oidcUser.getAuthorities());

            List<String> authRoles = oidcUser.getClaimAsStringList("auth_roles");
            if (authRoles != null) {
                authRoles.stream()
                        .map(role -> new SimpleGrantedAuthority(role))
                        .forEach(authorities::add);
            }

            return new DefaultOidcUser(
                    authorities,
                    oidcUser.getIdToken(),
                    oidcUser.getUserInfo()
            );
        };
    }
}
