package com.tasktrack.tasks.domain.auth.dto;

import com.tasktrack.tasks.domain.auth.entity.UserEntity;
import com.tasktrack.tasks.domain.auth.entity.UserRole;
import lombok.Builder;

import java.util.Map;

@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String username,
        String providerId,
        Map<String, Object> attributes
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            case "github" -> ofGithub(attributes);
            case "naver" -> ofNaver(attributes);
            default -> null;
        };
    }

    public static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .username("google_" + ((String) attributes.get("sub")))
                .providerId((String) attributes.get("sub"))
                .attributes(attributes)
                .build();
    }

    public static OAuth2UserInfo ofGithub(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .username("github_" + ((String) attributes.get("id")))
                .providerId((String) attributes.get("id"))
                .attributes(attributes)
                .build();
    }

    public static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2UserInfo.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .username("naver_" + ((String) response.get("id")))
                .providerId((String) response.get("id"))
                .attributes(response)
                .build();
    }

    public UserEntity toEntity() {
        return UserEntity.builder()
                .username(username)
                .name(name)
                .email(email)
                .userRole(UserRole.USER)
                .build();
    }
}
