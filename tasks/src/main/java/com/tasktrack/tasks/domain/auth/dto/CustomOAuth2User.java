package com.tasktrack.tasks.domain.auth.dto;

import com.tasktrack.tasks.domain.auth.entity.UserEntity;
import com.tasktrack.tasks.domain.auth.entity.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final UserEntity userEntity;

    private Map<String, Object> attributes;

    public CustomOAuth2User(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public CustomOAuth2User(UserEntity userEntity, Map<String, Object> attributes) {
        this.userEntity = userEntity;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getUserRole().toString();
            }
        });
        return list;
    }

    @Override
    public String getName() {
        return userEntity.getName();
    }

    public String getUserName() {
        return userEntity.getUsername();
    }
}
