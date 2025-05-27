package com.tasktrack.tasks.domain.oauth2User.dto;

public interface OAuth2Response {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
