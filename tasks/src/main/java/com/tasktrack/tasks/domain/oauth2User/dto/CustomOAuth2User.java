package com.tasktrack.tasks.domain.oauth2User.dto;

import com.tasktrack.tasks.domain.oauth2User.entity.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    @Getter
    private final String username;

    private final String name;

    private final UserRole userRole;

    public CustomOAuth2User(String username, String name, UserRole userRole) {
        this.username = username;
        this.name = name;
        this.userRole = userRole;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userRole.toString();
            }
        });
        return list;
    }

    @Override
    public String getName() {
        return this.name;
    }

}
