package com.example.spring_task_manager.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Position implements GrantedAuthority{
    DEVELOPER,
    TESTER,
    DEVOPS,
    MANAGER,
    TEAM_LEAD,
    QA,
    DEFAULT;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
