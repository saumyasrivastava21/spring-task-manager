package com.example.spring_task_manager.entity;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails, CredentialsContainer {
    private final AssignedUser assignedUser;
    private String password;
    public SecurityUser(AssignedUser assignedUser) {
        this.assignedUser = assignedUser;
        this.password = assignedUser.getPassword();
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(assignedUser.getPosition());
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return assignedUser.getEmail();
    }
}
